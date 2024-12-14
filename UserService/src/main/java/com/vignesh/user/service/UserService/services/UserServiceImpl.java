package com.vignesh.user.service.UserService.services;

import com.vignesh.user.service.UserService.entities.Hotel;
import com.vignesh.user.service.UserService.entities.Rating;
import com.vignesh.user.service.UserService.entities.User;
import com.vignesh.user.service.UserService.exceptions.ResourceNotFoundException;
import com.vignesh.user.service.UserService.external.services.HotelService;
import com.vignesh.user.service.UserService.external.services.RatingService;
import com.vignesh.user.service.UserService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RatingService ratingService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        String randomUid = UUID.randomUUID().toString();
        user.setUserId(randomUid);
        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public User getUser(String userId) {
        // Fetch the user from the database
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User With Given Id is Not found on Server"));

        // Fetch ratings for the user from Rating Service
        String ratingServiceUrl = "http://RATING-SERVICE/ratings/users/" + userId; // Corrected URL
        Rating[] ratingsOfUser = restTemplate.getForObject(ratingServiceUrl, Rating[].class);

        if (ratingsOfUser == null || ratingsOfUser.length == 0) {
            logger.info("No ratings found for user {}", userId);
        } else {
            logger.info("{} ratings fetched from the service", ratingsOfUser.length);
        }

        List<Rating> ratingList = Arrays.stream(ratingsOfUser)
                .map(rating -> {
                    try {
                        // Fetch hotel details and set on rating
                        Hotel hotel = hotelService.getHotel(rating.getHotelId());
                        rating.setHotel(hotel);
                    } catch (Exception e) {
                        logger.error("Error fetching hotel details for rating {}", rating.getId(), e);
                    }
                    return rating;
                })
                .collect(Collectors.toList());

        // Set ratings dynamically on the user object
        user.setRatings(ratingList);

        return user;
    }

    @Override
    public User updateUser(String userId, User users) {
        // Fetch the existing user from the database
        User existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User With Given Id is Not found on Server"));

        // Update the existing user's fields with the new user data
        existingUser.setName(users.getName());
        existingUser.setEmail(users.getEmail());
        existingUser.setAbout(users.getAbout());
        // Update other fields as necessary
        // For example: existingUser.setPhoneNumber(users.getPhoneNumber());

        // Save and return the updated user
        return userRepo.save(existingUser);
    }

    @Override
    public void deleteUser(String userId) {
        // Check if the user exists before attempting to delete
        User existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User With Given Id is Not found on Server"));

        // Delete the user
        userRepo.delete(existingUser);
    }

    @Override
    public User updateUserRating(String userId, String ratingId, Rating updatedRating) {
        Optional<User> userOptional = userRepo.findById(userId);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();

            // Fetch the updated rating from RatingService
            Rating updatedRatingFromService = ratingService.updateRating(ratingId, updatedRating);

            // Fetch ratings from RatingService
            String ratingServiceUrl = "http://RATING-SERVICE/ratings/users/" + userId;
            Rating[] ratingsOfUser = restTemplate.getForObject(ratingServiceUrl, Rating[].class);

            if (ratingsOfUser == null || ratingsOfUser.length == 0) {
                logger.info("No ratings found for user {}", userId);
            } else {
                logger.info("{} ratings fetched from service", ratingsOfUser.length);
            }

            // Update the rating list with the updated rating
            List<Rating> ratingList = Arrays.stream(ratingsOfUser)
                    .map(rating -> {
                        if (rating.getId().equals(ratingId)) {
                            try {
                                // Fetch hotel details and set it on the rating
                                Hotel hotel = hotelService.getHotel(rating.getHotelId());
                                rating.setHotel(hotel); // Set the hotel details on the rating
                            } catch (Exception e) {
                                logger.error("Error fetching hotel details for rating {}", rating.getId(), e);
                            }
                            // Replace the old rating with the updated one
                            updatedRatingFromService.setHotel(rating.getHotel()); // Ensure hotel info is kept
                            return updatedRatingFromService; // Return the updated rating with hotel
                        } else {
                            try {
                                // Fetch hotel details and set on other ratings as well
                                Hotel hotel = hotelService.getHotel(rating.getHotelId());
                                rating.setHotel(hotel);
                            } catch (Exception e) {
                                logger.error("Error fetching hotel details for rating {}", rating.getId(), e);
                            }
                        }
                        return rating; // Keep other ratings unchanged
                    })
                    .collect(Collectors.toList());

            // Set the updated ratings list to the user object
            existingUser.setRatings(ratingList);
            System.out.println(existingUser.getRatings());
            // Save and return the updated user entity
            return userRepo.save(existingUser);
        } else {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
    }
}
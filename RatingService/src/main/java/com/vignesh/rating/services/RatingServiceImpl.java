package com.vignesh.rating.services;

import com.vignesh.rating.entities.Rating;
import com.vignesh.rating.exceptions.ResourceNotFoundException;
import com.vignesh.rating.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RatingServiceImpl implements RatingService{

    @Autowired
    private RatingRepository repo;

    @Override
    public Rating createRating(Rating rating) {
        String randomUid = UUID.randomUUID().toString();
        rating.setId(randomUid);
        return repo.save(rating);
    }

    @Override
    public List<Rating> getAllRatings() {
        return repo.findAll();
    }

    @Override
    public List<Rating> getRatingsByUserId(String userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public List<Rating> getRatingsByHotelId(String id) {
        return repo.findByHotelId(id);
    }

    @Override
    public Rating getRatingsById(String id) {
        return repo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Rating with given Id not found"));

    }

    @Override
    public Rating updateRatingById(String id, Rating updatedRating) throws  CloneNotSupportedException {

        Rating existingRating = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + id));

        // Update fields
        existingRating.setRating(updatedRating.getRating());
        existingRating.setFeedback(updatedRating.getFeedback());

        return repo.save(existingRating);

    }
}

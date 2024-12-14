package com.vignesh.user.service.UserService.services;

import com.vignesh.user.service.UserService.entities.Rating;
import com.vignesh.user.service.UserService.entities.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    List<User> getAllUser();

    User getUser(String userId);

    User updateUser(String userId, User users);

    void deleteUser(String userId);

    User updateUserRating(String userId, String ratingId, Rating rating);


}

package com.vignesh.rating.services;

import com.vignesh.rating.entities.Rating;

import java.util.List;

public interface RatingService {

    Rating createRating(Rating rating);

    List<Rating> getAllRatings();

    List<Rating>

    getRatingsByUserId(String id);

    List<Rating> getRatingsByHotelId(String id);

    Rating getRatingsById(String id);

    Rating updateRatingById(String id,  Rating rating) throws  CloneNotSupportedException;
}

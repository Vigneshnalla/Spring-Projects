package com.vignesh.user.service.UserService.external.services;


import com.vignesh.user.service.UserService.entities.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Service
@FeignClient(name = "RATING-SERVICE")
public interface RatingService {


    //get

    //post
    @PostMapping("/ratings")
    public Rating createRating(Rating rating);

    //put
    @PutMapping("/ratings/{ratingId}")
    public  Rating updateRating(@PathVariable String ratingId, Rating rating);

    //delete("/ratings
    @DeleteMapping("/ratings/{ratingId}")
    public  Rating deleteRating(@PathVariable String ratingId);
}

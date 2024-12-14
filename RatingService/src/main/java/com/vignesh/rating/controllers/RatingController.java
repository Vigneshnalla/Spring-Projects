package com.vignesh.rating.controllers;


import com.vignesh.rating.entities.Rating;
import com.vignesh.rating.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import  java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping
    public ResponseEntity<Rating> create(@RequestBody Rating rating){
        System.out.println("ratings ");

        return  ResponseEntity.status(HttpStatus.CREATED).body(ratingService.createRating(rating));
    }

    @GetMapping
    public  ResponseEntity<List<Rating>> getAllRatings(){
        System.out.print("getall");
        return  ResponseEntity.ok(ratingService.getAllRatings());
    }
    @GetMapping("/hotels/{hotelId}")
    public  ResponseEntity<List<Rating>> getRatingByHotelId(@PathVariable String hotelId){
        return ResponseEntity.ok(ratingService.getRatingsByHotelId(hotelId));
    }

    @GetMapping("/users/{userId}")
    public  ResponseEntity<List<Rating>> getRatingByUserId(@PathVariable String userId){
        return ResponseEntity.ok(ratingService.getRatingsByUserId(userId));
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Rating> getRatingById(@PathVariable String id){
        return ResponseEntity.ok(ratingService.getRatingsById(id));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<Rating> updateRatingById(@PathVariable String id, @RequestBody Rating rating) throws CloneNotSupportedException{
        System.out.println("raing id "+id);
        System.out.println("raing id "+rating.getRating());
        return  ResponseEntity.ok(ratingService.updateRatingById(id,rating));
    }
}

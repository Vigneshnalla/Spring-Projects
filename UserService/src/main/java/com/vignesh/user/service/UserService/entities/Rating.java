package com.vignesh.user.service.UserService.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Rating {

    private String id;
    private String hotelId;
    private String userId;
    private int rating;
    private String feedback;


    private Hotel hotel;
}

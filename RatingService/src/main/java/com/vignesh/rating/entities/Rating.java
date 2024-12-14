package com.vignesh.rating.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("ratings")
public class Rating implements Cloneable{

    private String id;
    private String hotelId;
    private String userId;
    private int rating;
    private String feedback;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Rating copy() throws CloneNotSupportedException {
        return (Rating) clone();
    }



    private Hotel hotel;
}

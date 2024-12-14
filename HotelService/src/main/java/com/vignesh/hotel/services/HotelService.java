package com.vignesh.hotel.services;


import com.vignesh.hotel.entities.Hotel;

import java.util.List;

public interface HotelService {

    Hotel create(Hotel hotel);

    List<Hotel> getHotels();

    Hotel get(String id);

}

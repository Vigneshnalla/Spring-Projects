package com.vignesh.user.service.UserService.external.services;


import com.vignesh.user.service.UserService.entities.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "HOTEL-SERVICE")
public interface HotelService {

    @GetMapping("/hotels/{hotelId}") // API endpoint in the HOTEL-SERVICE application
    Hotel getHotel(@PathVariable("hotelId") String hotelId); // Proper mapping of the path variable
}

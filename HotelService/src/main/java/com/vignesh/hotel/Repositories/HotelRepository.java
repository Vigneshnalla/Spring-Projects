package com.vignesh.hotel.Repositories;

import com.vignesh.hotel.entities.Hotel;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, String> {
}

package com.veranum.hotel.repositories;

import com.veranum.hotel.models.HotelModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<HotelModel, Integer> {
}
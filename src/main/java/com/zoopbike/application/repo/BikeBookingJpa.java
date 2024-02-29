package com.zoopbike.application.repo;

import com.zoopbike.application.entity.BikeBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BikeBookingJpa extends JpaRepository<BikeBooking, UUID> {

}

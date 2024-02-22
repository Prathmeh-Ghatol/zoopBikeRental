package com.zoopbike.application.repo;

import com.zoopbike.application.entity.Bike;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface BikeRepo extends JpaRepository<Bike, UUID> {
}

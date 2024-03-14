package com.zoopbike.application.repo;

import com.zoopbike.application.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepo extends JpaRepository<Review , UUID> {

}

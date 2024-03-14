package com.zoopbike.application.entity;

import com.zoopbike.application.dto.BikeReturnDetailsDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Corrected GenerationType
    private UUID reviewId;

    @ManyToOne
    private ApplicationUser applicationUser;

    @ManyToOne
    private Bike bike;

    private String commentRealtedToBike;

    private String commentRealtedBikeProvider;

}

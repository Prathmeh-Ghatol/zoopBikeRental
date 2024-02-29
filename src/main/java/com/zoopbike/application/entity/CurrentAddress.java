package com.zoopbike.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CURRENT_ADDRESS")
public class CurrentAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID currentAddressId;

    private Long pincode;

    private String city;

    private String state;

    private String address;

    private String landmark;

    @OneToOne(mappedBy = "currentAddress")
    BikeProviderPartner bikeProviderPartnerForCurrentAddress;


    @OneToOne(mappedBy  ="currentAddress")
    ApplicationUser applicationUser;

    //@OneToMany
//    Set<ApplicationUserReview>userReviews=new HashSet<>();




}

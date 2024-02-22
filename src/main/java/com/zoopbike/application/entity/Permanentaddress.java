package com.zoopbike.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "PERMENT_ADDRESS")
public class Permanentaddress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID permanentAddressId;

    private Long pincode;

    private String city;

    private String state;

    private String address;

    private String landmark;

    @OneToOne(mappedBy = "permanentaddress")
    BikeProviderPartner bikeProviderPartner;

    @OneToOne(mappedBy = "permanentaddress")
    ApplicationUser applicationUser;
}

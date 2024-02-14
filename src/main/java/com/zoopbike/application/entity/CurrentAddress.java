package com.zoopbike.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
}

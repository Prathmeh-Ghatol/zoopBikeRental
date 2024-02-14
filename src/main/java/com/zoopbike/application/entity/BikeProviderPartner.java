package com.zoopbike.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BikeProviderPartner {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bikeProviderPartnerId;

    @Column(name = "Name")
    private String name;

    private String email;

    private String cellNumber;

    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private Permanentaddress permanentaddress;

    @OneToOne(cascade = CascadeType.ALL)
    private CurrentAddress currentAddress;
}

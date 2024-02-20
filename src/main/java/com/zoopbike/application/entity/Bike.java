package com.zoopbike.application.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bikeId;


    private String bikeName;

    @Column(unique = true,nullable = false)
    private String LicencePlate;

    @Column(nullable = false)
    private Date RegisteryDateRTO;

    private Date zoopRegisterDate;

    private String BikeCompnay;

    private Date pucValidity;
    @ManyToOne
    private BikeProviderPartner bikeProviderPartner;

}

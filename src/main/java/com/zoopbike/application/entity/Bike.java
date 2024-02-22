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
@Table(name = "BIKE")
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bikeId;
    private String bikeName;
    @Column(unique = true, nullable = false)
    private String licencePlate;
    @Column(nullable = false)
    private Date registeryDateRTO;
    private Date zoopRegisterDate;
    private String bikeBrand;
    private Date pucValidity;
    @ManyToOne
    private BikeProviderPartner bikeProviderPartner;
    private Boolean under_Maintenance;
    private Double currentMeterReading;
    private Integer freeDriveKm;
    private Double milage;
    private Double afterfreeDriveKmChargePerKm;
    private Boolean available;
    private Double pricePerDay;
    private

}

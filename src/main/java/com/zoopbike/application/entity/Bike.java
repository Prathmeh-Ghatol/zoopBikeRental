package com.zoopbike.application.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.zoopbike.application.utils.BikeType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BIKE_PARTNER_ID")
    private BikeProviderPartner bikeProviderPartner;
    private Boolean under_Maintenance;
    private Double currentMeterReading;
    private Integer freeDriveKm;
    private Double milage;
    private Double afterfreeDriveKmChargePerKm;
    private Double pricePerDay;
    @Enumerated(EnumType.STRING)
    private BikeType bikeType;
    private Double bikeFreeFuel;;
    @ManyToMany(mappedBy ="bikesBookReg",fetch = FetchType.LAZY)
    @JsonManagedReference
//    @JsonBackReference
    private List<BikeBooking>bikeBookings;
    private  Boolean bikeLocked;

}

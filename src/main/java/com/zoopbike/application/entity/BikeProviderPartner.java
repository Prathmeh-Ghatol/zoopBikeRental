package com.zoopbike.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.domain.Range;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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

    @Column(unique = true ,nullable = false)
    private String email;

    @Column(unique = true)
    private String cellNumber;

    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private Permanentaddress permanentaddress;

    @OneToOne(cascade = CascadeType.ALL)
    private CurrentAddress currentAddress;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "bikeProviderPartner")
    private Set<Bike> bikeOwner=new HashSet<>();

    private Double currentMeterReading;

    private Integer FreeDriveKm;

    private Double Milage;

    private Double AfterfreeDriveKmChargePerKm;

    private Boolean available;


}

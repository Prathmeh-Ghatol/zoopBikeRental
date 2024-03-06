package com.zoopbike.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.utils.BikeType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class BikeReturnDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID bikeId;
    private String bikeName;
    private String bikeBrand;
    private Date pucValidity;
    private Boolean Under_Maintenance;
    private Double currentMeterReading;
    private Integer FreeDriveKm;
    private Double Milage;
    private Double AfterfreeDriveKmChargePerKm;
    private double pricePerDay;
    private Double bikeFreeFuel;
    private BikeType bikeType;
    private Integer freeDriveKm;


}

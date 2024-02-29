package com.zoopbike.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zoopbike.application.utils.BikeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class BikeforBookingReturnDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID bikeId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String bikeName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String bikeBrand;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double Milage;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BikeType bikeType;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double pricePerDay;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double afterfreeDriveKmChargePerKm;





}

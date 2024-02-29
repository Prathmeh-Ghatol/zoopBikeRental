package com.zoopbike.application.dto;

import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.utils.BikeType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BikeDto {

    private String bikeName;
    private String licencePlate;
    private Date registeryDateRTO;
    private String bikeBrand;
    private Date pucValidity;
    private Boolean under_Maintenance;
    private Double currentMeterReading;
    private Integer freeDriveKm;
    private Double milage;
    private Double afterfreeDriveKmChargePerKm;
    private Boolean available;
    private Double pricePerDay;
    private BikeType bikeType;
    private Double bikeFreeFuel;


}

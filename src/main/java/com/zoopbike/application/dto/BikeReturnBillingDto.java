package com.zoopbike.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BikeReturnBillingDto {
    private UUID bookingId;
    private BikeReturnDto bikeReturnDto;
    private Double drivenKm;
    private LocalDateTime returnDate;
    private LocalDateTime returnDateAsPerBooking;
    private Double freeKm;
    private Double priceToPay;
    private Double BookTimePrice;
    private Double afterReturnBikeMeterReading;
    private Double BookingBikeMeterReading;
}

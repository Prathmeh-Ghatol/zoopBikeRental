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
public class BikeStatusForBikeProvider {
    private UUID bookingId;
    private LocalDateTime bookingDate;
    private LocalDateTime endBookingDate;
    private BikeforBookingReturnDto bike;
    private Double Bookedprice;
    private Double pricePaid;
    private Double kmDriven;
    private ApplicationUserDto applicationUserDto;
}

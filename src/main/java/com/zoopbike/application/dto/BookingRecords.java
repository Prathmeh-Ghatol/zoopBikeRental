package com.zoopbike.application.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.zoopbike.application.entity.ApplicationUser;
import com.zoopbike.application.entity.Bike;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRecords {
        private UUID bookingId;
        private LocalDateTime bookingDate;
        private LocalDateTime endBookingDate;
        private BikeforBookingReturnDto bike;
        private Double Bookedprice;
        private Double pricePaid;
        private Double kmDriven;
        private CurrentAddressDto pickUpLocation;

}

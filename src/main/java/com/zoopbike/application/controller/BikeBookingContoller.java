package com.zoopbike.application.controller;

import com.zoopbike.application.dto.BikeEstimadePaymentBeforeBooked;
import com.zoopbike.application.dto.BookDto;
import com.zoopbike.application.dto.BookingRecords;
import com.zoopbike.application.entity.BikeBooking;
import com.zoopbike.application.service.impl.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/booking/bike")
public class BikeBookingContoller {

    @Autowired
    private BookingService bookingService;
    @PostMapping(value = "/estimated/bikeId/{bikeId}/ApplicationUserId/{ApplicationUserId}")
   public ResponseEntity<BikeEstimadePaymentBeforeBooked>estimade(@PathVariable("bikeId")UUID uuid,
                                          @PathVariable("ApplicationUserId") UUID applicationId
                                         , @RequestBody BookDto bookDot) {
        BikeEstimadePaymentBeforeBooked bikeEstimadePaymentBeforeBooked=bookingService.paymentDetails(uuid, applicationId, bookDot);
        return ResponseEntity.status(HttpStatus.OK).body(bikeEstimadePaymentBeforeBooked);


    }
    @PostMapping(value = "/booked/bikeId/{bikeId}/ApplicationUserId/{ApplicationUserId}")
    public ResponseEntity<BookingRecords>BookBike(@PathVariable("bikeId")UUID uuid,
                                               @PathVariable("ApplicationUserId") UUID applicationId
            , @RequestBody BookDto bookDot) {
        BookingRecords booked =bookingService.bookingBike(uuid, applicationId, bookDot);
        return ResponseEntity.status(HttpStatus.OK).body(booked);


    }

}

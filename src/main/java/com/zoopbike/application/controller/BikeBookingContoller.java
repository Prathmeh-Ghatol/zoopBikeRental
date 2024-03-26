package com.zoopbike.application.controller;

import com.zoopbike.application.dto.BikeEstimadePaymentBeforeBooked;
import com.zoopbike.application.dto.BookDto;
import com.zoopbike.application.dto.BookingRecords;
import com.zoopbike.application.entity.BikeBooking;
import com.zoopbike.application.service.impl.ApplicationUserserviceImpl;
import com.zoopbike.application.service.impl.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/booking/bike")
public class BikeBookingContoller {

    @Autowired
    private BookingService bookingService;
    @Autowired
    ApplicationUserserviceImpl applicationUserservice;
    @PostMapping(value = "/estimated/bikeId/{bikeId}/ApplicationUserId/{ApplicationUserId}")
    @PreAuthorize("hasRole('ROLE_APPLICATION_USER')")
    public ResponseEntity<BikeEstimadePaymentBeforeBooked>estimade(@PathVariable("bikeId")UUID uuid,
                                          @PathVariable("ApplicationUserId") UUID applicationId
                                         , @RequestBody BookDto bookDot) {
        BikeEstimadePaymentBeforeBooked bikeEstimadePaymentBeforeBooked=bookingService.paymentDetails(uuid, applicationId, bookDot);
        return ResponseEntity.status(HttpStatus.OK).body(bikeEstimadePaymentBeforeBooked);


    }
    @PostMapping(value = "/booked/bikeId/{bikeId}/ApplicationUserId/{ApplicationUserId}")
    @PreAuthorize("hasRole('ROLE_APPLICATION_USER')")
    public ResponseEntity<BookingRecords>BookBike(@PathVariable("bikeId")UUID uuid,
                                               @PathVariable("ApplicationUserId") UUID applicationId
            , @RequestBody BookDto bookDot) {
        BookingRecords booked =bookingService.bookingBike(uuid, applicationId, bookDot);
        return ResponseEntity.status(HttpStatus.OK).body(booked);


    }

    @PostMapping(value = "/cancelled/{bookingId}/applicationId/{applicationId}")
    @PreAuthorize("hasRole('ROLE_APPLICATION_USER')")
    public  ResponseEntity<Map<String ,Boolean>>cancelledBooking(@PathVariable("bookingId") UUID bookingId ,@PathVariable("applicationId") UUID applicationId){
        Boolean bookingStatus=this.applicationUserservice.cancelledBooking(bookingId,applicationId);
        HashMap<String,Boolean>returnDetails=new HashMap<>();
        returnDetails.put("Booking cancelled",bookingStatus);
        return ResponseEntity.status(HttpStatus.OK).body(returnDetails);
    }


    @GetMapping(value = "/get/booking/id/{bookingId}")
    @PreAuthorize("hasRole('ROLE_APPLICATION_USER') and hasRole('ROLE_BIKEPROVIDER_USER') and hasRole('ROLE_ADMIN')")

    public ResponseEntity<BookingRecords>getBooking(@PathVariable("bookingId") UUID bookingId){
        return  ResponseEntity.ok(this.bookingService.getBookingRecordsById(bookingId));
    }

}

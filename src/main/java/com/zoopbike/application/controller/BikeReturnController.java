package com.zoopbike.application.controller;


import com.zoopbike.application.dto.BikeReturnBillingDto;
import com.zoopbike.application.dto.BikeReturnDetailsDto;
import com.zoopbike.application.service.impl.BikeReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/return/bike")
public class BikeReturnController {

    @Autowired
    BikeReturnService bikeReturnService;
    @PostMapping(value = "/bookingId/{bookingId}/user/{applicationUserId}")
    public ResponseEntity<BikeReturnBillingDto> bikeReturn(@RequestBody BikeReturnDetailsDto bikeReturnDetailsDto,
                                                          @PathVariable("bookingId") UUID bookingId,
                                                          @PathVariable("applicationUserId") UUID applicationUserId){

       return ResponseEntity.status(HttpStatus.OK).body(this.bikeReturnService.returnBikeBooking(bookingId,applicationUserId, bikeReturnDetailsDto));


    }


}
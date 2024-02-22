package com.zoopbike.application.controller;

import com.zoopbike.application.dto.BikeDto;
import com.zoopbike.application.dto.BikeReturnDto;
import com.zoopbike.application.service.BikeService;
import com.zoopbike.application.service.impl.BikeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController

@RequestMapping(value = "/bike/service")
public class BikeController {

    @Autowired
    private BikeServiceImpl bikeService;

    @PostMapping(value = "/add/{bikeVenderEmail}")
    public ResponseEntity<BikeReturnDto>addBike(@RequestBody BikeDto bikeDto, @PathVariable("bikeVenderEmail") String bikeVenderEmail){
       System.out.println(bikeDto);
        BikeReturnDto bike=  this.bikeService.addBike(bikeDto, bikeVenderEmail);
       return ResponseEntity.status(HttpStatus.CREATED).body(bike);
    }

    @PutMapping(value = "/update/bike/status/{uuid}")
    public ResponseEntity<BikeReturnDto>updateStatus(@RequestBody BikeDto bikeDto, @PathVariable("uuid")UUID bikeID){
        BikeReturnDto bikeReturnDto=this.bikeService.updateBike(bikeDto, bikeID);
        return ResponseEntity.status(HttpStatus.OK).body(bikeReturnDto);
    }

}

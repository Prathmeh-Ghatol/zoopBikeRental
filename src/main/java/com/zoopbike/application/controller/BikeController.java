package com.zoopbike.application.controller;

import com.zoopbike.application.dto.BikeDto;
import com.zoopbike.application.dto.BikeReturnDto;
import com.zoopbike.application.dto.GenricPage;
import com.zoopbike.application.service.impl.BikeSeImpl;
import com.zoopbike.application.service.impl.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.zoopbike.application.utils.zoopBikeRentalApplicationConstant.defaultApplicationPageSize;
import static com.zoopbike.application.utils.zoopBikeRentalApplicationConstant.defualtApplicationPageNO;

@RestController

@RequestMapping(value = "/bike/service")
public class BikeController {

    @Autowired
    BikeSeImpl bikeService;

    @Autowired
    BookingService bookingService;
    @PostMapping(value = "/add/{bikeVenderEmail}")
    public ResponseEntity<BikeReturnDto>addBike(@RequestBody BikeDto bikeDto, @PathVariable("bikeVenderEmail") String bikeVenderEmail) throws InterruptedException {
        BikeReturnDto bike=  this.bikeService.addBike(bikeDto, bikeVenderEmail);
       return ResponseEntity.status(HttpStatus.CREATED).body(bike);
    }

    @PutMapping(value = "/update/status/{uuid}")
    public ResponseEntity<BikeReturnDto>updateStatus(@RequestBody BikeDto bikeDto, @PathVariable("uuid")UUID bikeID){
        BikeReturnDto bikeReturnDto=this.bikeService.updateBike(bikeDto, bikeID);
        return ResponseEntity.status(HttpStatus.OK).body(bikeReturnDto);
    }

    @GetMapping(value = "/get/{uuid}")
    public  ResponseEntity<BikeReturnDto>getBike(@PathVariable("uuid")UUID bikeId){
        BikeReturnDto bike =this.bikeService.getBikeById(bikeId);
        return ResponseEntity.status(HttpStatus.OK).body(bike);
    }

    @DeleteMapping(value = "/delete/{uuid}")
    public  ResponseEntity<Boolean>deleteBike(@PathVariable("uuid")UUID bikeId){
        Boolean bike =this.bikeService.deleteBike(bikeId);
        return ResponseEntity.status(HttpStatus.OK).body(bike);
    }
    @GetMapping(value = "/get/all/bikeprovider/{email}")
    public  ResponseEntity<GenricPage<BikeReturnDto>>getAllBikes(
            @RequestParam(value = "pageNo",defaultValue =defualtApplicationPageNO,required = false)int pageNo,
            @RequestParam(value = "pageSize",defaultValue = defaultApplicationPageSize,required = false) int pageSize,
            @PathVariable("email")String bikeProviderEmail){
        GenricPage<BikeReturnDto>bikes =this.bikeService.getAllBikeOfBikeVender(bikeProviderEmail,pageNo,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(bikes);
    }

}

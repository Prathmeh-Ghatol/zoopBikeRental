package com.zoopbike.application.controller;

import com.zoopbike.application.dto.BikeProviderPartnerDto;
import com.zoopbike.application.service.impl.BikePartnerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/bikepartner")
public class BikeProviderController {

    @Autowired
    BikePartnerServiceImpl bikePartnerService;
    @PostMapping(value = "/register")
    public ResponseEntity<BikeProviderPartnerDto>register(@RequestBody BikeProviderPartnerDto bikeProviderPattnerDto){
    System.out.println(bikeProviderPattnerDto);
     BikeProviderPartnerDto dto=   bikePartnerService.register(bikeProviderPattnerDto);
     return ResponseEntity.status(HttpStatus.CREATED).body(dto);

    }
    @DeleteMapping(value = "/deregister/{Id}")
    public ResponseEntity<HashMap<Boolean,String>>deRegister(@PathVariable("Id") UUID id ){
        HashMap<Boolean,String>deleteObject=new HashMap<>();
        Boolean flag=this.bikePartnerService.deregister(id);
        deleteObject.put(flag, "BikePartner is removed sucssesfully" + id);
        return ResponseEntity.status(HttpStatus.OK).body( deleteObject);
    }
}

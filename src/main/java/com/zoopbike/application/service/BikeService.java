package com.zoopbike.application.service;

import com.zoopbike.application.dto.BikeDto;
import com.zoopbike.application.dto.BikeReturnDto;

import java.util.UUID;

public interface BikeService {

     public BikeReturnDto addBike(BikeDto bikeDto, String email);

     public BikeReturnDto updateBike (BikeDto bikeDto , UUID bikeId);


     public Boolean deleteBike (UUID bikeId);
     public BikeReturnDto getBikeById(UUID bikeID);



     }

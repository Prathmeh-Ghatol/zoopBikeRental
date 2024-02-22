package com.zoopbike.application.service.impl;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.zoopbike.application.dto.BikeDto;
import com.zoopbike.application.dto.BikeProviderPartnerDto;
import com.zoopbike.application.dto.BikeReturnDto;
import com.zoopbike.application.entity.Bike;
import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.exception.BadBikeException;
import com.zoopbike.application.exception.BikeProviderPartnerException;
import com.zoopbike.application.repo.BikePartnerRepo;
import com.zoopbike.application.repo.BikeRepo;
import com.zoopbike.application.service.BikePartnerService;
import com.zoopbike.application.service.BikeService;
import com.zoopbike.application.utils.CacheStore;
import com.zoopbike.application.utils.ObjectMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
@Service

public class BikeServiceImpl implements BikeService {
    private BikeRepo bikeRepo;
    private ObjectMappingService objectMappingService;
    private CacheStore cacheStore;
    private BikePartnerRepo bikePartnerRepo;
    @Autowired
    BikeServiceImpl(BikePartnerServiceImpl bikePartnerService, BikeRepo bikeRepo, ObjectMappingService objectMappingService, CacheStore cacheStore,BikePartnerRepo bikePartnerRepo){
        this.bikeRepo =bikeRepo;
        this.objectMappingService=objectMappingService;
        this.cacheStore=cacheStore;
        this.bikePartnerRepo=bikePartnerRepo;
    }
    @Override
    public BikeReturnDto addBike(BikeDto bikeDto, String email) {
        BikeProviderPartner bikeProviderPartner=bikePartnerRepo.findBikeProviderPartner(email);
        if(bikeProviderPartner==null){
            throw new BikeProviderPartnerException("The Bike Vender not found with " +email, "Bike Vender");
        }
        Bike bike=new Bike();
        bike.setAfterfreeDriveKmChargePerKm(10.0);
        bike.setAvailable(true);
        bike.setBikeBrand(bikeDto.getBikeBrand());
        bike.setBikeName(bikeDto.getBikeName());
        bike.setBikeProviderPartner(bikeProviderPartner);
        bike.setCurrentMeterReading(bikeDto.getCurrentMeterReading());
        bike.setFreeDriveKm(100);
        bike.setLicencePlate(bikeDto.getLicencePlate());
        bike.setMilage(bikeDto.getMilage());
        bike.setPucValidity(bikeDto.getPucValidity());
        bike.setRegisteryDateRTO(bikeDto.getRegisteryDateRTO());
        bike.setUnder_Maintenance(false);
        bike.setZoopRegisterDate(new Date());
        bike.setPricePerDay(bikeDto.getPricePerDay());
        Bike bikeSaved=this.bikeRepo.save(bike);
        cacheStore.bikesCache.put(bikeSaved.getBikeId(),bikeSaved);
        return this.objectMappingService.entityToPojo(bikeSaved, BikeReturnDto.class);

   }

    @Override
    public BikeReturnDto updateBike(BikeDto bikeDto, UUID bikeId) {
        Optional<Bike>bike=this.bikeRepo.findById(bikeId);
        Bike bikeUpdatBike=null;
        if(!bike.isEmpty()){
            bikeUpdatBike=bike.get();
            bikeUpdatBike.setPucValidity(bikeDto.getPucValidity());
            bikeUpdatBike.setUnder_Maintenance(bikeDto.getUnder_Maintenance());
            bikeUpdatBike.setAvailable(bikeDto.getAvailable());
        }else {
            throw new BadBikeException("BIke is not found with id " + bike, "BIKE");
        }
        Bike bikeUpdate=this.bikeRepo.save(bikeUpdatBike);
        return this.objectMappingService.entityToPojo(bikeUpdate, BikeReturnDto.class);
    }

    @Override
    public BikeReturnDto deleteBike(UUID bikeId) {
        return null;
    }
}

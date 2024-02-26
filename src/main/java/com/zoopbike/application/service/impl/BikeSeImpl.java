package com.zoopbike.application.service.impl;

import com.zoopbike.application.dto.BikeDto;
import com.zoopbike.application.dto.BikeReturnDto;
import com.zoopbike.application.dto.GenricPage;
import com.zoopbike.application.entity.Bike;
import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.exception.BadBikeException;
import com.zoopbike.application.exception.BikeProviderPartnerException;
import com.zoopbike.application.repo.BikePartnerRepo;
import com.zoopbike.application.repo.BikeRepo;
import com.zoopbike.application.service.BikeService;
import com.zoopbike.application.utils.CacheStore;
import com.zoopbike.application.utils.ObjectMappingService;
import jakarta.persistence.GeneratedValue;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.internal.util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BikeSeImpl implements BikeService {

    private BikeRepo bikeRepo;
    private ObjectMappingService objectMappingService;
    private CacheStore cacheStore;
    private BikePartnerRepo bikePartnerRepo;

    @Autowired
    BikeSeImpl(BikePartnerServiceImpl bikePartnerService, BikeRepo bikeRepo, ObjectMappingService objectMappingService, CacheStore cacheStore, BikePartnerRepo bikePartnerRepo) {
        this.bikeRepo = bikeRepo;
        this.objectMappingService = objectMappingService;
        this.cacheStore = cacheStore;
        this.bikePartnerRepo = bikePartnerRepo;
    }

    @Override
    @Transactional
    public BikeReturnDto addBike(BikeDto bikeDto, String email) {
        BikeProviderPartner bikeProviderPartner = bikePartnerRepo.findBikeProviderPartner(email);
        if (bikeProviderPartner == null) {
            throw new BikeProviderPartnerException("The Bike Vender not found with " + email, "Bike Vender");
        }
        Bike bike = new Bike();
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
        bike.setBikeFreeFuel(bikeDto.getBikeFreeFuel());
        bike.setBikeType(bikeDto.getBikeType());
        Bike bikeSaved = this.bikeRepo.save(bike);
        System.out.println(bikeSaved);
        cacheStore.bikesCache.put(bikeSaved.getBikeId(), bikeSaved);
        return this.objectMappingService.entityToPojo(bikeSaved, BikeReturnDto.class);

    }

    @Override
    @Transactional
    public BikeReturnDto updateBike(BikeDto bikeDto, UUID bikeId) {
        Optional<Bike> bike = this.bikeRepo.findById(bikeId);
        Bike bikeUpdatBike = null;
        if (!bike.isEmpty()) {
            bikeUpdatBike = bike.get();
            bikeUpdatBike.setPucValidity(bikeDto.getPucValidity());
            bikeUpdatBike.setUnder_Maintenance(bikeDto.getUnder_Maintenance());
            bikeUpdatBike.setAvailable(bikeDto.getAvailable());
            bikeUpdatBike.setAfterfreeDriveKmChargePerKm(bikeDto.getAfterfreeDriveKmChargePerKm());

            if(bike.get().getCurrentMeterReading()<=bikeDto.getCurrentMeterReading()){
            bikeUpdatBike.setCurrentMeterReading(bikeDto.getCurrentMeterReading());
            }else {
                throw new BadBikeException("Meter Reading is not proper", "bike");
            }
            bikeUpdatBike.setFreeDriveKm(bikeDto.getFreeDriveKm());
            bikeUpdatBike.setMilage(bikeDto.getMilage());
            bikeUpdatBike.setBikeFreeFuel(bikeDto.getBikeFreeFuel());
        } else {
            throw new BadBikeException("Bike is not found with id " + bike, "BIKE");
        }
        Bike bikeUpdate = this.bikeRepo.save(bikeUpdatBike);
        cacheStore.bikesCache.put(bikeUpdate.getBikeId(), bikeUpdate);
        return this.objectMappingService.entityToPojo(bikeUpdate, BikeReturnDto.class);
    }


    @Transactional
    @Override
    public Boolean deleteBike(UUID bikeId) {
        Optional<Bike> bike = this.bikeRepo.findById(bikeId);
        Boolean deleteFlag = false;
        if (bike.isEmpty()) {
            throw new BadBikeException("Bike is not present " + bikeId, "bike");
        }
        this.bikeRepo.deleteById(bikeId);
        Optional<Bike> bikeReturn = this.bikeRepo.findById(bikeId);
        if (bike.isEmpty()) {
            deleteFlag = true;
        }
        cacheStore.bikesCache.remove(bikeId);
        return deleteFlag;
    }

    @Transactional
    public BikeReturnDto getBikeById(UUID bikeID) {
        Bike bike=null;
        if (bikeID != null) {
            bike=this.cacheStore.bikesCache.get(bikeID);
            if(bike==null){
                bike=this.bikeRepo.findById(bikeID).orElseThrow(
                        ()->new BadBikeException("Bike is not present with this " + bikeID,"BIKE")
                );
            }
        }

        return objectMappingService.entityToPojo(bike,BikeReturnDto.class);
        }

    @Transactional
    public GenricPage<BikeReturnDto> getAllBikeOfBikeVender(String BikeVenderEmail,int pageSize,int pageNo){
       BikeProviderPartner bikeProviderPartner= this.bikePartnerRepo.findBikeProviderPartner(BikeVenderEmail);
       if(bikeProviderPartner==null){
           throw new BikeProviderPartnerException("Bike Provider is not avilable with email" + BikeVenderEmail,"Bike Provider Partner");
       }
        Pageable pageable = PageRequest.of(0, 5);
             Page<Bike>allBikes= this.bikeRepo.getAllBikeOfBikeProvider(pageable,bikeProviderPartner.getBikeProviderPartnerId());
                List<Bike>bikes=allBikes.getContent();
               List<BikeReturnDto> bikeReturnDto= bikes.stream().map(bike -> this.objectMappingService.entityToPojo(bike, BikeReturnDto.class)).collect(Collectors.toList());

        GenricPage<BikeReturnDto> genricPage=new GenricPage();
        synchronized (bikeProviderPartner.getBikeProviderPartnerId()){
                 genricPage.setContent(bikeReturnDto);
                 genricPage.setIsLastPage(allBikes.isLast());
                 genricPage.setPageNo(allBikes.getNumber());
                 genricPage.setPageSize(allBikes.getSize());
                 genricPage.setTotalPage(allBikes.getTotalPages());
                 genricPage.setTotalElement(allBikes.getTotalElements());
             }
             return genricPage;

    }
}

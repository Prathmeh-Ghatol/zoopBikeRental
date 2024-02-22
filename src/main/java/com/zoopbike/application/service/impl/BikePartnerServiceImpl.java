package com.zoopbike.application.service.impl;

import com.zoopbike.application.dto.BikeProviderPartnerDto;
import com.zoopbike.application.dto.CurrentAddressDto;
import com.zoopbike.application.dto.PermenetAddressDto;
import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.entity.CurrentAddress;
import com.zoopbike.application.entity.Permanentaddress;
import com.zoopbike.application.exception.BadaddressException;
import com.zoopbike.application.exception.BikeProviderPartnerException;
import com.zoopbike.application.repo.BikePartnerRepo;
import com.zoopbike.application.repo.CurrentAddressRepo;
import com.zoopbike.application.repo.PermentAddressRepo;
import com.zoopbike.application.service.BikePartnerService;
import com.zoopbike.application.utils.CacheStore;
import com.zoopbike.application.utils.ObjectMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class BikePartnerServiceImpl implements BikePartnerService {
    private Map<String,BikeProviderPartner>bikeProviderPartnercache=new HashMap<>();
    @Autowired
    private BikePartnerRepo bikePartnerRepo;
    @Autowired
    private ObjectMappingService mappingService;
    @Autowired
    private PermentAddressRepo permenetAddressRepo;

    @Autowired
    private CurrentAddressRepo currentAddressRepo;

    @Autowired
    private CacheStore cacheStore;

    @Override
    public BikeProviderPartnerDto register(BikeProviderPartnerDto bikeProviderPartnerDto) {
           if(bikeProviderPartnercache.containsKey(bikeProviderPartnerDto.getEmail())){
            throw new BikeProviderPartnerException("bikeprovider already exist with this mail  please login " + bikeProviderPartnerDto.getEmail(),
                    "BikeProviderPartner");
        }
        BikeProviderPartner bikeProviderPartner = mappingService.pojoToentity(bikeProviderPartnerDto, BikeProviderPartner.class);
        PermenetAddressDto permenetAddressDto = bikeProviderPartnerDto.getPermenetAddressDto();
        Permanentaddress permanentaddress = mappingService.pojoToentity(permenetAddressDto, Permanentaddress.class);
        permanentaddress.setBikeProviderPartner(bikeProviderPartner);
        bikeProviderPartner.setPermanentaddress(permanentaddress);
        CurrentAddressDto currentAddressDto=bikeProviderPartnerDto.getCurrentAddressDto();
         CurrentAddress currentAddress=null;
        if (bikeProviderPartnerDto.getCurrentAddressSameToPermentAddress() == true &&
                currentAddressDto==null) {
            currentAddress = this.mappingService.permeantAddressTocurrentAddress(permanentaddress, CurrentAddress.class);

            if (currentAddress != null) {
                currentAddress.setBikeProviderPartnerForCurrentAddress(bikeProviderPartner);
                bikeProviderPartner.setCurrentAddress(currentAddress);
            }
        }else if(currentAddressDto!=null){
           currentAddress=this.mappingService.pojoToentity(currentAddressDto, CurrentAddress.class);
           bikeProviderPartner.setCurrentAddress(currentAddress);
           currentAddress.setBikeProviderPartnerForCurrentAddress(bikeProviderPartner);
        }else if(bikeProviderPartnerDto.getCurrentAddressSameToPermentAddress().equals(false)&&
            bikeProviderPartnerDto.getCurrentAddressDto()==null){
            throw new BadaddressException("Please Provide current address",null);

        }
        bikeProviderPartner = this.bikePartnerRepo.save(bikeProviderPartner);
        Optional<Permanentaddress> permanentaddressOptional = permenetAddressRepo.findById(bikeProviderPartner.getPermanentaddress().getPermanentAddressId());
        PermenetAddressDto permenetAddressDtoReturn = null;
        if (!permanentaddressOptional.isEmpty()) {
            permenetAddressDtoReturn = this.mappingService.entityToPojo(permanentaddressOptional.get(), PermenetAddressDto.class);

        }


        Optional<CurrentAddress>currentAddressForCoconversion=this.currentAddressRepo.findById(bikeProviderPartner.getCurrentAddress().getCurrentAddressId());
        CurrentAddressDto currentAddressDto2 = null;
        if (currentAddressForCoconversion.get() != null) {
            currentAddressDto2= this.mappingService.entityToPojo(currentAddressForCoconversion.get(), CurrentAddressDto.class);

        }


        BikeProviderPartnerDto dto = this.mappingService.entityToPojo(bikeProviderPartner, BikeProviderPartnerDto.class);
        dto.setPermenetAddressDto(permenetAddressDtoReturn);
        dto.setCurrentAddressDto(currentAddressDto2);
        bikeProviderPartnercache.put(bikeProviderPartner.getEmail(),bikeProviderPartner);
        return dto;

    }

    @Override
    public Boolean deregister(UUID id) {
        Optional<BikeProviderPartner> bikeProviderPartner = this.bikePartnerRepo.findById(id);
        Boolean removeFlag = false;
        if (!bikeProviderPartner.isEmpty()) {
            bikePartnerRepo.deleteById(id);
            Optional<BikeProviderPartner> bikeProviderPartnerAfterRemove = this.bikePartnerRepo.findById(id);
            if (!bikeProviderPartnerAfterRemove.isEmpty()) {
                removeFlag = true;
            }
        }
        return removeFlag;
    }

    public BikeProviderPartnerDto getBikeProviderPartnerByEmailorVenderId(String email,UUID bikeproviderVenderUUID){
        BikeProviderPartner bikeProviderPartner=null;
        if(email!=null){
            bikeProviderPartner=cacheStore.bikeProviderPartnerMap.get(email);
            if(bikeProviderPartner!=null && bikeproviderVenderUUID.equals(null)){
                return this.mappingService.entityToPojo(bikeProviderPartner,BikeProviderPartnerDto.class);
            } else if (bikeProviderPartner==null) {
            bikeProviderPartner= this.bikePartnerRepo.findBikeProviderPartner(email);
                CurrentAddress currentAddress=bikeProviderPartner.getCurrentAddress();
                CurrentAddressDto currentAddressDto=this.mappingService.entityToPojo(currentAddress,CurrentAddressDto.class);
                Permanentaddress permanentaddress=bikeProviderPartner.getPermanentaddress();
                PermenetAddressDto permenetAddressDto=this.mappingService.entityToPojo(permanentaddress,PermenetAddressDto.class);
                BikeProviderPartnerDto bikeProviderPartnerDto= this.mappingService.entityToPojo(bikeProviderPartner,BikeProviderPartnerDto.class);

                bikeProviderPartnerDto.setCurrentAddressDto(currentAddressDto);
                bikeProviderPartnerDto.setPermenetAddressDto(permenetAddressDto);
                return bikeProviderPartnerDto;

            }else {
                throw new BikeProviderPartnerException("Bike Provider Partner not found !!", email);
            }
        }else if(bikeproviderVenderUUID!=null && email.isEmpty()){
            Optional<BikeProviderPartner>bikeProviderPartnerOptional=this.bikePartnerRepo.findById(bikeproviderVenderUUID);
            if(!bikeProviderPartnerOptional.isEmpty()){
                bikeProviderPartner=bikeProviderPartnerOptional.get();
                CurrentAddress currentAddress=bikeProviderPartner.getCurrentAddress();
                CurrentAddressDto currentAddressDto=this.mappingService.entityToPojo(currentAddress,CurrentAddressDto.class);
                Permanentaddress permanentaddress=bikeProviderPartner.getPermanentaddress();
                PermenetAddressDto permenetAddressDto=this.mappingService.entityToPojo(permanentaddress,PermenetAddressDto.class);
                BikeProviderPartnerDto bikeProviderPartnerDto= this.mappingService.entityToPojo(bikeProviderPartner,BikeProviderPartnerDto.class);
                bikeProviderPartnerDto.setCurrentAddressDto(currentAddressDto);
                bikeProviderPartnerDto.setPermenetAddressDto(permenetAddressDto);
                return bikeProviderPartnerDto;

            }else{
                throw new BikeProviderPartnerException("Bike Provider Partner not found !!",String.valueOf( bikeproviderVenderUUID));
            }
        }
        System.out.println("********************** " +bikeProviderPartner.getBikeOwner());
        return this.mappingService.entityToPojo(bikeProviderPartner,BikeProviderPartnerDto.class);
    }


    public BikeProviderPartnerDto update(String email,BikeProviderPartnerDto bikeProviderPartnerDto){
        BikeProviderPartner bikeProviderPartner=bikeProviderPartnercache.get(email);
        BikeProviderPartnerDto bikeProviderPartnerDtoReturn=null;
        if(bikeProviderPartner!=null ){
            bikeProviderUpdate(bikeProviderPartner, bikeProviderPartnerDto);
        }
        bikeProviderPartner= this.bikePartnerRepo.findBikeProviderPartner(email);
        if (bikeProviderPartner!=null) {
            bikeProviderUpdate(bikeProviderPartner, bikeProviderPartnerDto);
        }
        else {
            throw new BikeProviderPartnerException("Bike Provider not exist with this mail " + bikeProviderPartnerDto.getEmail(), "BikeProviderPartner");
        }
        return bikeProviderPartnerDtoReturn;

    }
    private BikeProviderPartner bikeProviderUpdate(BikeProviderPartner  bikeProvider ,BikeProviderPartnerDto bikeProviderPartnerDto){

        bikeProvider.setCellNumber(bikeProviderPartnerDto.getCellNumber());
        bikeProvider.setCellNumber(bikeProviderPartnerDto.getCellNumber());
        bikeProvider.setEmail(bikeProvider.getEmail());
        bikeProvider.setName(bikeProvider.getName());
        bikeProvider.setPassword(bikeProvider.getPassword());
        CurrentAddress currentAddress= this.mappingService.pojoToentity(bikeProviderPartnerDto.getCurrentAddressDto(), CurrentAddress.class);
        Permanentaddress permanentaddress = this.mappingService.pojoToentity(bikeProviderPartnerDto.getPermenetAddressDto(), Permanentaddress.class);
        bikeProvider.setCurrentAddress(currentAddress);
        bikeProvider.setPermanentaddress(permanentaddress);
        BikeProviderPartner bikeProviderPartnerUpdated=this.bikePartnerRepo.save(bikeProvider);
        return bikeProviderPartnerUpdated;

    }

    }


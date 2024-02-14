package com.zoopbike.application.service.impl;

import com.zoopbike.application.dto.BikeProviderPartnerDto;
import com.zoopbike.application.dto.CurrentAddressDto;
import com.zoopbike.application.dto.PermenetAddressDto;
import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.entity.CurrentAddress;
import com.zoopbike.application.entity.Permanentaddress;
import com.zoopbike.application.repo.BikePartnerRepo;
import com.zoopbike.application.repo.CurrentAddressRepo;
import com.zoopbike.application.repo.PermentAddressRepo;
import com.zoopbike.application.service.BikePartnerService;
import com.zoopbike.application.utils.ObjectMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BikePartnerServiceImpl implements BikePartnerService {
    @Autowired
    BikePartnerRepo bikePartnerRepo;
    @Autowired
    ObjectMappingService mappingService;
    @Autowired
    PermentAddressRepo permenetAddressRepo;

    @Autowired
    CurrentAddressRepo currentAddressRepo;

    @Override
    public BikeProviderPartnerDto register(BikeProviderPartnerDto bikeProviderPartnerDto) {
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
        }else {

            throw new RuntimeException("Please click proper option ");
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
}

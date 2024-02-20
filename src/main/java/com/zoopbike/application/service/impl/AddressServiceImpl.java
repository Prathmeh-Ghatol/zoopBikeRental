package com.zoopbike.application.service.impl;

import com.zoopbike.application.dto.CurrentAddressDto;
import com.zoopbike.application.dto.PermenetAddressDto;
import com.zoopbike.application.entity.ApplicationUser;
import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.entity.CurrentAddress;
import com.zoopbike.application.entity.Permanentaddress;
import com.zoopbike.application.exception.ApplicationUserException;
import com.zoopbike.application.exception.BikeProviderPartnerException;
import com.zoopbike.application.repo.ApplicationUserRepo;
import com.zoopbike.application.repo.BikePartnerRepo;
import com.zoopbike.application.repo.CurrentAddressRepo;
import com.zoopbike.application.repo.PermentAddressRepo;
import com.zoopbike.application.utils.CacheStore;
import com.zoopbike.application.utils.ObjectMappingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements com.zoopbike.application.service.AddressService {

    private ApplicationUserRepo applicationUserRepo;
    private BikePartnerRepo bikePartnerRepo;
    private ObjectMappingService objectMappingService;
    private PermentAddressRepo permentAddressRepo;
    private  CurrentAddressRepo currentAddressRepo;
    private  CacheStore cacheStore;
     @Autowired
     AddressServiceImpl (ApplicationUserRepo applicationUserRepo, BikePartnerRepo bikePartnerRepo, ObjectMappingService objectMappingService, CurrentAddressRepo currentAddressRepo
                                       , PermentAddressRepo permentAddressRepo,CacheStore cacheStore ){
        this.applicationUserRepo=applicationUserRepo;
        this.bikePartnerRepo=bikePartnerRepo;
        this.objectMappingService=objectMappingService;
        this.currentAddressRepo=currentAddressRepo;
        this.permentAddressRepo=permentAddressRepo;
        this.cacheStore=cacheStore;
    }
    @Override
    @Transactional
    public CurrentAddressDto updateCurrentAddressForBikeProviderPartner(CurrentAddressDto currentAddressDto, String email) {
        BikeProviderPartner bikeProviderPartner= this.bikePartnerRepo.findBikeProviderPartner(email);
        if(bikeProviderPartner==null){
            throw new BikeProviderPartnerException("The BikeProvider Partner not found with email id " , email);
        }
        CurrentAddress currentAddress=bikeProviderPartner.getCurrentAddress();
        CurrentAddress currentAddressUpdate=this.objectMappingService.pojoToentity(currentAddressDto,CurrentAddress.class);
        currentAddress.setAddress(currentAddressUpdate.getAddress());
        currentAddress.setBikeProviderPartnerForCurrentAddress(bikeProviderPartner);
        currentAddress.setCity(currentAddressUpdate.getCity());
        currentAddress.setState(currentAddressUpdate.getState());
        currentAddress.setLandmark(currentAddressUpdate.getLandmark());
        currentAddress.setPincode(currentAddress.getPincode());
        bikeProviderPartner.setCurrentAddress(currentAddress);
        BikeProviderPartner bikeProviderPartnerSaved=this.bikePartnerRepo.save(bikeProviderPartner);
        CurrentAddress currentAddressSaved=this.currentAddressRepo.save(currentAddress);
        if(cacheStore.bikeProviderPartnerMap.containsKey(bikeProviderPartnerSaved.getEmail())){
            cacheStore.bikeProviderPartnerMap.put(bikeProviderPartnerSaved.getEmail(),bikeProviderPartnerSaved);
        }


        return   this.objectMappingService.pojoToentity(currentAddressSaved, CurrentAddressDto.class);
    }

    @Override
    @Transactional
    public PermenetAddressDto updatepermentAddressForBikeProviderPartner(PermenetAddressDto permenetAddressDto, String email) {
        BikeProviderPartner bikeProviderPartner= this.bikePartnerRepo.findBikeProviderPartner(email);
        if(bikeProviderPartner==null){
            throw new BikeProviderPartnerException("The BikeProvider Partner not found with email id " , email);
        }
        Permanentaddress permanentaddress=bikeProviderPartner.getPermanentaddress();
        PermenetAddressDto permenetAddressDto1=this.objectMappingService.pojoToentity(permenetAddressDto,PermenetAddressDto.class);
        permanentaddress.setAddress(permenetAddressDto1.getAddress());
        permanentaddress.setBikeProviderPartner(bikeProviderPartner);
        permanentaddress.setCity(permenetAddressDto1.getCity());
        permanentaddress.setState(permenetAddressDto1.getState());
        permanentaddress.setLandmark(permenetAddressDto1.getLandmark());
        permanentaddress.setPincode(permenetAddressDto1.getPincode());
        Permanentaddress parmentAddressSaved=this.permentAddressRepo.save(permanentaddress);
        bikeProviderPartner.setPermanentaddress(parmentAddressSaved);
        this.bikePartnerRepo.save(bikeProviderPartner);
        if(cacheStore.bikeProviderPartnerMap.containsKey(bikeProviderPartner.getEmail())){
            cacheStore.bikeProviderPartnerMap.put(bikeProviderPartner.getEmail(),bikeProviderPartner);
        }

        return this.objectMappingService.entityToPojo(parmentAddressSaved, PermenetAddressDto.class);
    }

    @Override
    @Transactional
    public PermenetAddressDto updatepermanentAddressForApplicationUser(PermenetAddressDto permenetAddressDto, String email) {

        ApplicationUser applicationUser= this.applicationUserRepo.findApplicationUserByEmail(email);
        if(applicationUser==null){
            throw new ApplicationUserException("The user not found with email id " , email);
        }
        Permanentaddress permanentaddress=applicationUser.getPermanentaddress();
        Permanentaddress permenetAddressDtoUpdate=this.objectMappingService.pojoToentity(permenetAddressDto,Permanentaddress.class);
        permanentaddress.setAddress(permenetAddressDtoUpdate.getAddress());
        permanentaddress.setBikeProviderPartner(null);
        permanentaddress.setCity(permenetAddressDtoUpdate.getCity());
        permanentaddress.setState(permenetAddressDtoUpdate.getState());
        permanentaddress.setLandmark(permenetAddressDtoUpdate.getLandmark());
        permanentaddress.setPincode(permenetAddressDtoUpdate.getPincode());
        permanentaddress.setState(permenetAddressDtoUpdate.getState());
        applicationUser.setPermanentaddress(permenetAddressDtoUpdate);
        this.applicationUserRepo.save(applicationUser);
        if(cacheStore.applicationUserMap.containsKey(applicationUser.getEmail())){
            cacheStore.applicationUserMap.put(applicationUser.getEmail(),applicationUser);
        }
        Permanentaddress permanentaddressSavesd=this.permentAddressRepo.save(permenetAddressDtoUpdate);
        return   this.objectMappingService.pojoToentity(permanentaddressSavesd, PermenetAddressDto.class);
    }




    @Override
    public CurrentAddressDto updatecurrentAddressForApplicationUser(CurrentAddressDto currentAddressDto, String email) {
        ApplicationUser applicationUser= this.applicationUserRepo.findApplicationUserByEmail(email);
        if(applicationUser==null){
            throw new ApplicationUserException("The user not found with email id " , email);
        }
        CurrentAddress currentAddress=applicationUser.getCurrentAddress();
        CurrentAddress currentAddressUpdate=this.objectMappingService.pojoToentity(currentAddressDto,CurrentAddress.class);
        currentAddress.setAddress(currentAddressUpdate.getAddress());
        currentAddress.setBikeProviderPartnerForCurrentAddress(null);
        currentAddress.setCity(currentAddressUpdate.getCity());
        currentAddress.setState(currentAddressUpdate.getState());
        currentAddress.setLandmark(currentAddressUpdate.getLandmark());
        currentAddress.setPincode(currentAddressUpdate.getPincode());
        applicationUser.setCurrentAddress(currentAddressUpdate);
        this.applicationUserRepo.save(applicationUser);
        if(cacheStore.applicationUserMap.containsKey(applicationUser.getEmail())){
            cacheStore.applicationUserMap.put(applicationUser.getEmail(),applicationUser);
        }
        CurrentAddress currentAddressSaved=this.currentAddressRepo.save(currentAddress);
        return   this.objectMappingService.pojoToentity(currentAddressSaved, CurrentAddressDto.class);
    }
}

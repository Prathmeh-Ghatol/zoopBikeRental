package com.zoopbike.application.service.impl;

import com.zoopbike.application.dto.*;
import com.zoopbike.application.entity.*;
import com.zoopbike.application.exception.BadaddressException;
import com.zoopbike.application.exception.BikeProviderPartnerException;
import com.zoopbike.application.repo.BikePartnerRepo;
import com.zoopbike.application.repo.CurrentAddressRepo;
import com.zoopbike.application.repo.PermentAddressRepo;
import com.zoopbike.application.service.BikePartnerService;
import com.zoopbike.application.utils.CacheStore;
import com.zoopbike.application.utils.ObjectMappingService;
import com.zoopbike.application.utils.zoopBikeRentalApplicationConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.zoopbike.application.utils.zoopBikeRentalApplicationConstant.RoleMaps;


@Service
public class BikePartnerServiceImpl implements BikePartnerService {
    private Map<String, BikeProviderPartner> bikeProviderPartnercache = new HashMap<>();
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

    @Autowired
    private ApplicationUserserviceImpl applicationUserservice;

    @Autowired
    private zoopBikeRentalApplicationConstant zoopBikeRentalApplicationConstant;

    @Override
    public BikeProviderPartnerDto register(BikeProviderPartnerDto bikeProviderPartnerDto) {
        if (bikeProviderPartnercache.containsKey(bikeProviderPartnerDto.getEmail())) {
            throw new BikeProviderPartnerException("bikeprovider already exist with this mail  please login " + bikeProviderPartnerDto.getEmail(),
                    "BikeProviderPartner");
        }
        BikeProviderPartner bikeProviderPartner = mappingService.pojoToentity(bikeProviderPartnerDto, BikeProviderPartner.class);
//        bikeProviderPartner.setPassword(passwordEncoder.encode(bikeProviderPartner.getPassword()));
        PermenetAddressDto permenetAddressDto = bikeProviderPartnerDto.getPermenetAddressDto();
        Permanentaddress permanentaddress = mappingService.pojoToentity(permenetAddressDto, Permanentaddress.class);
        permanentaddress.setBikeProviderPartner(bikeProviderPartner);
        bikeProviderPartner.setPermanentaddress(permanentaddress);
        bikeProviderPartner.setRoles(Collections.singletonList(RoleMaps.get("ROLE_BIKEPROVIDER_USER")));

        CurrentAddressDto currentAddressDto = bikeProviderPartnerDto.getCurrentAddressDto();
        CurrentAddress currentAddress = null;
        if (bikeProviderPartnerDto.getCurrentAddressSameToPermentAddress() == true &&
                currentAddressDto == null) {
            currentAddress = this.mappingService.permeantAddressTocurrentAddress(permanentaddress, CurrentAddress.class);

            if (currentAddress != null) {
                currentAddress.setBikeProviderPartnerForCurrentAddress(bikeProviderPartner);
                bikeProviderPartner.setCurrentAddress(currentAddress);
                bikeProviderPartner.setLocked(false);


            }
        } else if (currentAddressDto != null) {
            currentAddress = this.mappingService.pojoToentity(currentAddressDto, CurrentAddress.class);
            bikeProviderPartner.setCurrentAddress(currentAddress);
            currentAddress.setBikeProviderPartnerForCurrentAddress(bikeProviderPartner);
        } else if (bikeProviderPartnerDto.getCurrentAddressSameToPermentAddress().equals(false) &&
                bikeProviderPartnerDto.getCurrentAddressDto() == null) {
            throw new BadaddressException("Please Provide current address", null);

        }
        bikeProviderPartner = this.bikePartnerRepo.save(bikeProviderPartner);
        Optional<Permanentaddress> permanentaddressOptional = permenetAddressRepo.findById(bikeProviderPartner.getPermanentaddress().getPermanentAddressId());
        PermenetAddressDto permenetAddressDtoReturn = null;
        if (!permanentaddressOptional.isEmpty()) {
            permenetAddressDtoReturn = this.mappingService.entityToPojo(permanentaddressOptional.get(), PermenetAddressDto.class);

        }


        Optional<CurrentAddress> currentAddressForCoconversion = this.currentAddressRepo.findById(bikeProviderPartner.getCurrentAddress().getCurrentAddressId());
        CurrentAddressDto currentAddressDto2 = null;
        if (currentAddressForCoconversion.get() != null) {
            currentAddressDto2 = this.mappingService.entityToPojo(currentAddressForCoconversion.get(), CurrentAddressDto.class);

        }


        BikeProviderPartnerDto dto = this.mappingService.entityToPojo(bikeProviderPartner, BikeProviderPartnerDto.class);
        dto.setPermenetAddressDto(permenetAddressDtoReturn);
        dto.setCurrentAddressDto(currentAddressDto2);
        bikeProviderPartnercache.put(bikeProviderPartner.getEmail(), bikeProviderPartner);
        return dto;

    }

    @Override
    public Boolean deregister(UUID id) {
            Optional<BikeProviderPartner> bikeProviderPartnerOptional = this.bikePartnerRepo.findById(id);
            if (bikeProviderPartnerOptional.isPresent()) {
                BikeProviderPartner bikeProviderPartner = bikeProviderPartnerOptional.get();
                this.bikePartnerRepo.deleteById(id);
                // Remove from cache
                cacheStore.bikeProviderPartnerMap.remove(bikeProviderPartner.getEmail());
                // Check if the entity is still present after deletion
                return !this.bikePartnerRepo.findById(id).isPresent();
            }
            return false;
        }


        public BikeProviderPartnerDto getBikeProviderPartnerByEmailorVenderId(String email, UUID bikeproviderVenderUUID) {
        BikeProviderPartner bikeProviderPartner = null;
        if (email != null) {
            bikeProviderPartner = cacheStore.bikeProviderPartnerMap.get(email);
            if (bikeProviderPartner != null && bikeproviderVenderUUID.equals(null)) {

                BikeProviderPartnerDto bikeProviderPartnerDto=mappingService.entityToPojo(bikeProviderPartner, BikeProviderPartnerDto.class);
                bikeProviderPartnerDto.setCurrentAddressDto(this.mappingService.entityToPojo(bikeProviderPartner.getCurrentAddress(),CurrentAddressDto.class));
                bikeProviderPartnerDto.setPermenetAddressDto(this.mappingService.entityToPojo(bikeProviderPartner.getPermanentaddress(),PermenetAddressDto.class));
                return bikeProviderPartnerDto;
            }
            else if (bikeProviderPartner == null) {
                bikeProviderPartner = this.bikePartnerRepo.findBikeProviderPartner(email);
                CurrentAddress currentAddress = bikeProviderPartner.getCurrentAddress();
                CurrentAddressDto currentAddressDto = this.mappingService.entityToPojo(currentAddress, CurrentAddressDto.class);
                Permanentaddress permanentaddress = bikeProviderPartner.getPermanentaddress();
                PermenetAddressDto permenetAddressDto = this.mappingService.entityToPojo(permanentaddress, PermenetAddressDto.class);
                BikeProviderPartnerDto bikeProviderPartnerDto = this.mappingService.entityToPojo(bikeProviderPartner, BikeProviderPartnerDto.class);

                bikeProviderPartnerDto.setCurrentAddressDto(currentAddressDto);
                bikeProviderPartnerDto.setPermenetAddressDto(permenetAddressDto);
                cacheStore.bikeProviderPartnerMap.put(bikeProviderPartner.getEmail(),bikeProviderPartner);
                return bikeProviderPartnerDto;

            } else {
                throw new BikeProviderPartnerException("Bike Provider Partner not found !!", email);
            }
        } else if (bikeproviderVenderUUID != null && email.isEmpty()) {
            Optional<BikeProviderPartner> bikeProviderPartnerOptional = this.bikePartnerRepo.findById(bikeproviderVenderUUID);
            if (!bikeProviderPartnerOptional.isEmpty()) {
                bikeProviderPartner = bikeProviderPartnerOptional.get();
                CurrentAddress currentAddress = bikeProviderPartner.getCurrentAddress();
                CurrentAddressDto currentAddressDto = this.mappingService.entityToPojo(currentAddress, CurrentAddressDto.class);
                Permanentaddress permanentaddress = bikeProviderPartner.getPermanentaddress();
                PermenetAddressDto permenetAddressDto = this.mappingService.entityToPojo(permanentaddress, PermenetAddressDto.class);
                BikeProviderPartnerDto bikeProviderPartnerDto = this.mappingService.entityToPojo(bikeProviderPartner, BikeProviderPartnerDto.class);
                bikeProviderPartnerDto.setCurrentAddressDto(currentAddressDto);
                bikeProviderPartnerDto.setPermenetAddressDto(permenetAddressDto);
                return bikeProviderPartnerDto;

            } else {
                throw new BikeProviderPartnerException("Bike Provider Partner not found !!", String.valueOf(bikeproviderVenderUUID));
            }
        }
        return this.mappingService.entityToPojo(bikeProviderPartner, BikeProviderPartnerDto.class);
    }


    public BikeProviderPartnerDto update(String email, BikeProviderPartnerDto bikeProviderPartnerDto) {
        BikeProviderPartner bikeProviderPartner = bikeProviderPartnercache.get(email);
        BikeProviderPartnerDto bikeProviderPartnerDtoReturn = null;
        if (bikeProviderPartner != null) {
            bikeProviderUpdate(bikeProviderPartner, bikeProviderPartnerDto);
        }
        bikeProviderPartner = this.bikePartnerRepo.findBikeProviderPartner(email);
        if (bikeProviderPartner != null) {
            bikeProviderUpdate(bikeProviderPartner, bikeProviderPartnerDto);
        } else {
            throw new BikeProviderPartnerException("Bike Provider not exist with this mail " + bikeProviderPartnerDto.getEmail(), "BikeProviderPartner");
        }
        return bikeProviderPartnerDtoReturn;

    }

    private BikeProviderPartner bikeProviderUpdate(BikeProviderPartner bikeProvider, BikeProviderPartnerDto bikeProviderPartnerDto) {

        bikeProvider.setCellNumber(bikeProviderPartnerDto.getCellNumber());
        bikeProvider.setCellNumber(bikeProviderPartnerDto.getCellNumber());
        bikeProvider.setEmail(bikeProvider.getEmail());
        bikeProvider.setName(bikeProvider.getName());
        bikeProvider.setPassword(bikeProvider.getPassword());
        bikeProvider.setIsAvilable(bikeProviderPartnerDto.getIsAvilable());
        //CurrentAddress currentAddress= this.mappingService.pojoToentity(bikeProviderPartnerDto.getCurrentAddressDto(), CurrentAddress.class);
        //Permanentaddress permanentaddress = this.mappingService.pojoToentity(bikeProviderPartnerDto.getPermenetAddressDto(), Permanentaddress.class);
        //bikeProvider.setCurrentAddress(currentAddress);
        //bikeProvider.setPermanentaddress(permanentaddress);
        BikeProviderPartner bikeProviderPartnerUpdated = this.bikePartnerRepo.save(bikeProvider);
        return bikeProviderPartnerUpdated;

    }
//    public List<BikeBooking> getBookedBikeStatus(UUID BikeProviderId) {
//        BikeProviderPartner bikeProvider = this.bikePartnerRepo.findById(BikeProviderId)
//                .orElseThrow(() -> new BikeProviderPartnerException("Bike Provider is not available with id : " + BikeProviderId, "BikeProvider"));
//
//        Set<Bike> allBike = bikeProvider.getBikeOwner();
//        List<BikeBooking> allBook = allBike.stream().
//                map(bike -> bike.getBikeBookings()).collect(Collectors.toList());
//                .map(bikeBookings -> {
//                    List<BikeStatusForBikeProvider> allCurrentBooking = new ArrayList<>();
//
//                    for (BikeBooking bikeBooking : bikeBookings) {
//                        BikeStatusForBikeProvider bikeStatusForBikeProvider = new BikeStatusForBikeProvider();
//                        if (validateForCurrentBooking(bikeBooking.getTillDate())) {
//                            bikeStatusForBikeProvider.setApplicationUserDto(this.mappingService
//                                    .entityToPojo(bikeBooking.getApplicationUserBikeBook(), ApplicationUserDto.class));
//
//                            bikeStatusForBikeProvider.setBookingId(bikeBooking.getBookingId());
//                            bikeStatusForBikeProvider.setBookingDate(bikeBooking.getDateBook());
//                            bikeStatusForBikeProvider.setKmDriven(bikeBooking.getKmDriven());
//                            bikeStatusForBikeProvider.setBookedprice(bikeBooking.getBookedprice());
//                            bikeStatusForBikeProvider.setEndBookingDate(bikeBooking.getTillDate());
//                            bikeStatusForBikeProvider.setPricePaid(bikeBooking.getPricePaid());
//                        }
//                        allCurrentBooking.add(bikeStatusForBikeProvider);
//                    }
//                    return allCurrentBooking;
//                }).collect(Collectors.toList());
//        return allBook;
//    }
// }
    private boolean validateForCurrentBooking(LocalDateTime tillBooking) {

        return zoopBikeRentalApplicationConstant.getCurrentDateTime().isBefore(tillBooking);

    }
}
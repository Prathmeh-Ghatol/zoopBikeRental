package com.zoopbike.application.service.impl;

import com.zoopbike.application.dto.BikeReturnDto;
import com.zoopbike.application.dto.BookDto;
import com.zoopbike.application.entity.Bike;
import com.zoopbike.application.entity.BikeBooking;
import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.entity.CurrentAddress;
import com.zoopbike.application.exception.BikeSearchingException;
import com.zoopbike.application.exception.BookingException;
import com.zoopbike.application.repo.BikePartnerRepo;
import com.zoopbike.application.repo.BikeRepo;
import com.zoopbike.application.repo.CurrentAddressRepo;
import com.zoopbike.application.service.BikeService;
import com.zoopbike.application.utils.ObjectMappingService;
import org.modelmapper.internal.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BikeFindingServiceImpl {

    @Autowired
    private BikePartnerRepo bikePartnerRepo;
    @Autowired
    private ObjectMappingService objectMappingService;

    @Autowired
    BikeRepo bikeRepo;

    @Autowired
    CurrentAddressRepo currentAddressRepo;

    public List<BikeReturnDto> bikefindingProcess(String city, int pageNo, int PageSize, BookDto bookDto) {
        if(bookDto!=null){
            if(bookDto.getBookingDate().isAfter(bookDto.getEndBookingDate())){
                throw new BikeSearchingException("Please Enter Proper Exception","TIME");
            }
        }
        Pageable pageable = PageRequest.of(pageNo, PageSize);
        Page<CurrentAddress> cityCurrentAdd = this.currentAddressRepo.getAllBikeFromCity(city, pageable);
        List<CurrentAddress> cityCurrentAddress = cityCurrentAdd.getContent();
        List<BikeProviderPartner> allBikeProviderWhichAvilable = new ArrayList<>();
        for (CurrentAddress currentAddress : cityCurrentAddress) {
            BikeProviderPartner bikeProviderPartner = this.bikePartnerRepo.getAll(currentAddress.getCurrentAddressId());
            if (bikeProviderPartner.getIsAvilable() == true) {

                allBikeProviderWhichAvilable.add(bikeProviderPartner);
            }
        }
        List<Set<Bike>> bikesOnRent = new ArrayList<>();
        allBikeProviderWhichAvilable.forEach(bikeProviderPartner -> {
            Set<Bike> bikeSet = bikeProviderPartner.getBikeOwner();
            if (bikeSet != null) {
                bikesOnRent.add(bikeSet);
            }
        });

        List<BikeReturnDto> allBikes = new ArrayList<>();
        for (Set<Bike> bikeSet : bikesOnRent) {
            for (Bike bike : bikeSet) {
                Bike bikeAvilable=parseBikeForAvailability(bike, bookDto.getBookingDate(), bookDto.getEndBookingDate()) ;
                if(bikeAvilable!=null){
                    BikeReturnDto bikeReturnDto = objectMappingService.entityToPojo(bikeAvilable, BikeReturnDto.class);
                    allBikes.add(bikeReturnDto);
                }
            }


        }
        if(allBikes.isEmpty()){
            throw new BikeSearchingException("Sorry !! no bike avilable at this time", "Bike");
        }
        return allBikes;
    }



    public Bike parseBikeForAvailability(Bike bike, LocalDateTime bookingDate, LocalDateTime tillBookedDate) {
        if (bike == null || bookingDate == null || tillBookedDate == null) {
            throw new IllegalArgumentException("Bike, booking date, and till booked date cannot be null");
        }

        List<BikeBooking> bookings = bike.getBikeBookings();

        List<BikeBooking> overlappingBookings = bookings.stream()
                .filter(booking -> booking.getDateBook().isBefore(tillBookedDate) && booking.getTillDate().isAfter(bookingDate))
                .collect(Collectors.toList());
        if (!overlappingBookings.isEmpty()) {
            return null;
        }
        return bike;
    }
}
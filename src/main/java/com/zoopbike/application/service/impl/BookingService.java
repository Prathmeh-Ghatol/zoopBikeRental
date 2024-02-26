package com.zoopbike.application.service.impl;

import com.zoopbike.application.dto.*;
import com.zoopbike.application.entity.ApplicationUser;
import com.zoopbike.application.entity.Bike;
import com.zoopbike.application.entity.BikeBooking;
import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.exception.ApplicationUserException;
import com.zoopbike.application.exception.BadBikeException;
import com.zoopbike.application.exception.BikeProviderPartnerException;
import com.zoopbike.application.repo.ApplicationUserRepo;
import com.zoopbike.application.repo.BikeBookingJpa;
import com.zoopbike.application.repo.BikePartnerRepo;
import com.zoopbike.application.repo.BikeRepo;
import com.zoopbike.application.service.BikeBookingService;
import com.zoopbike.application.utils.ObjectMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

@Service
public class BookingService implements BikeBookingService {

    private ApplicationUserRepo applicationUserRepo;
    private BikePartnerRepo bikePartnerRepo;
    private BikeBookingJpa bikeBookingJpa;

    private BikeRepo bikeRepo;
    private ObjectMappingService objectMappingService;

    @Autowired
    public BookingService(ApplicationUserRepo applicationUserRepo, BikePartnerRepo bikePartnerRepo,
                          BikeBookingJpa bikeBookingJpa, BikeRepo bikeRepo, ObjectMappingService objectMappingService) {
        this.applicationUserRepo = applicationUserRepo;
        this.bikePartnerRepo = bikePartnerRepo;
        this.bikeBookingJpa = bikeBookingJpa;
        this.bikeRepo = bikeRepo;
        this.objectMappingService = objectMappingService;
    }

    @Override
    public BikeBooking bookingBike(UUID bikeId, UUID applicationId, BookDto bookDto) {
        ApplicationUser applicationUser = this.applicationUserRepo.findById(applicationId).orElseThrow(() -> new ApplicationUserException("User not exist with this id" + applicationId, "ApplicationUser"));
        Bike bike = this.bikeRepo.findById(bikeId).orElseThrow(() -> new BadBikeException("Bike is not present with " + bikeId, "BIKE"));
        Boolean isAvilable = bike.getBikeProviderPartner().getIsAvilable();
        if (bike.getBikeProviderPartner().getIsAvilable() == false) {
            throw new BikeProviderPartnerException("This type Bike Provider is not avilable", "BIKE PROVIDER");
        }
        BikeBooking bikeBookingSaved = null;

        if (bike.getAvailable().equals(true) && bike.getBikeProviderPartner().getIsAvilable().equals(true)
                && bike.getUnder_Maintenance().equals(false)) {
            BikeBooking bikeBooking = new BikeBooking();
            bikeBooking.setDateBook(bookDto.getBookingDate());
            bikeBooking.setTillDate(bookDto.getEndBookingDate());
            bikeBooking.setApplicationUserBikeBook(Collections.singletonList(applicationUser));
            bikeBooking.setBikesBookReg(Collections.singletonList(bike));
            bikeBookingSaved = this.bikeBookingJpa.save(bikeBooking);
            bike.setAvailable(false);
            this.bikeRepo.save(bike);

        } else {
            throw new BadBikeException("Bike is alredy booked", "bike");
        }
        return bikeBookingSaved;

    }

    public BikeEstimadePaymentBeforeBooked paymentDetails(UUID bikeId, UUID applicationId, BookDto bookDto) {

        ApplicationUser applicationUser = this.applicationUserRepo.findById(applicationId).orElseThrow(() -> new ApplicationUserException("User not exist with this id" + applicationId, "ApplicationUser"));
        Bike bike = this.bikeRepo.findById(bikeId).orElseThrow(() -> new BadBikeException("Bike is not present with " + bikeId, "BIKE"));
        if (bike.getAvailable().equals(true) && bike.getBikeProviderPartner().getIsAvilable() == true && bike.getUnder_Maintenance().equals(false)) {

            if (bookDto == null) {
                System.out.println("Booking issue");
            }
            System.out.println(bookDto);
            Double pricePerDay = bike.getPricePerDay();

            HashMap<String, Long> timing = TimeCalculation(bookDto.getBookingDate(), bookDto.getEndBookingDate());

            long days = timing.get("days");
            long hour = timing.get("hours");
            if (days < 0 && hour < 0) {
                        throw new DateTimeException("Please select proper date ");
            }
                if (hour > 0 && days == 0) {
                    days = 1;
                }
                if (hour > 8) {
                    days = days + 1;
                }
                Double amountNeedToPay = days * pricePerDay;
                BikeEstimadePaymentBeforeBooked bikeEstimadePaymentBeforeBooked = new BikeEstimadePaymentBeforeBooked();
                BikeReturnDto bikeDto = this.objectMappingService.entityToPojo(bike, BikeReturnDto.class);
                bikeEstimadePaymentBeforeBooked.setBike(bikeDto);
                bikeEstimadePaymentBeforeBooked.setBookingDate(bookDto.getBookingDate());
                bikeEstimadePaymentBeforeBooked.setEndBookingDate(bookDto.getEndBookingDate());
                bikeEstimadePaymentBeforeBooked.setPrice(amountNeedToPay);
                BikeProviderPartner bikeProviderPartner = bike.getBikeProviderPartner();
                bikeEstimadePaymentBeforeBooked.setPickupLoLocation(objectMappingService.entityToPojo(bikeProviderPartner.getCurrentAddress(), CurrentAddressDto.class));
                return bikeEstimadePaymentBeforeBooked;

            }
        else {
            throw new BadBikeException("Bike is alredy booked" + bike.getBikeId(), "bike");
        }
    }


    private HashMap<java.lang.String, Long> TimeCalculation(LocalDateTime bookedDay, LocalDateTime lastdate) {
        Duration duration = Duration.between(bookedDay, lastdate);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        HashMap<String, Long> time = new HashMap<>();
        time.put("days", days);
        time.put("hours", hours);
        return time;
    }


}

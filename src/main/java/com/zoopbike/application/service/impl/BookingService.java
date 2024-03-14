package com.zoopbike.application.service.impl;

import com.zoopbike.application.dto.*;
import com.zoopbike.application.entity.ApplicationUser;
import com.zoopbike.application.entity.Bike;
import com.zoopbike.application.entity.BikeBooking;
import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.exception.ReviewException;
import com.zoopbike.application.exception.BadBikeException;
import com.zoopbike.application.exception.BikeProviderPartnerException;
import com.zoopbike.application.exception.BookingException;
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
import java.util.*;

import static com.zoopbike.application.utils.zoopBikeRentalApplicationConstant.zeroDay;
import static com.zoopbike.application.utils.zoopBikeRentalApplicationConstant.zeroHour;

@Service
public class BookingService implements BikeBookingService {

    private ApplicationUserRepo applicationUserRepo;
    private BikePartnerRepo bikePartnerRepo;
    private BikeBookingJpa bikeBookingJpa;

    private BikeRepo bikeRepo;
    private ObjectMappingService objectMappingService;

    private BikeFindingServiceImpl bikeFindingService;
    @Autowired
    public BookingService(ApplicationUserRepo applicationUserRepo, BikePartnerRepo bikePartnerRepo,
                          BikeBookingJpa bikeBookingJpa, BikeRepo bikeRepo, ObjectMappingService objectMappingService,
                          BikeFindingServiceImpl bikeFindingService) {
        this.applicationUserRepo = applicationUserRepo;
        this.bikePartnerRepo = bikePartnerRepo;
        this.bikeBookingJpa = bikeBookingJpa;
        this.bikeRepo = bikeRepo;
        this.objectMappingService = objectMappingService;
        this.bikeFindingService=bikeFindingService;
    }

    @Override
    public BookingRecords bookingBike(UUID bikeId, UUID applicationId, BookDto bookDto) {
        ApplicationUser applicationUser = this.applicationUserRepo.findById(applicationId).orElseThrow(() -> new ReviewException("User not exist with this id" + applicationId, "ApplicationUser"));
        Bike bike = this.bikeRepo.findById(bikeId).orElseThrow(() -> new BadBikeException("Bike is not present with " + bikeId, "BIKE"));
//        Boolean isAvilable = bike.getBikeProviderPartner().getIsAvilable();
        if (bike.getBikeProviderPartner().getIsAvilable() == false) {
            throw new BikeProviderPartnerException("This type Bike Provider is not avilable", "BIKE PROVIDER");
        }
        Bike bikeAvibility= this.bikeFindingService.parseBikeForAvailability(bike,bookDto.getBookingDate(),bookDto.getEndBookingDate());
        if(bikeAvibility==null && bike.getBikeLocked()){
            throw new BookingException("Bike is not avilable for this date", "Bike");
        }
        BikeBooking bikeBookingSaved = null;
        HashMap<String, Long> timing = TimeCalculation(bookDto.getBookingDate(), bookDto.getEndBookingDate());
        Double amountNeedToPay= priceNeedToPay(timing,bike.getPricePerDay());

        if ( bike.getBikeProviderPartner().getIsAvilable().equals(true)
                && bike.getUnder_Maintenance().equals(false)) {
            BikeBooking bikeBooking = new BikeBooking();
            bikeBooking.setDateBook(bookDto.getBookingDate());
            bikeBooking.setTillDate(bookDto.getEndBookingDate());
            bikeBooking.setApplicationUserBikeBook(Collections.singletonList(applicationUser));
            bikeBooking.setBikesBookReg(Collections.singletonList(bike));
            bikeBooking.setBooking_Cancelled(false);
            bikeBooking.setBookedprice(amountNeedToPay);
            bikeBookingSaved = this.bikeBookingJpa.save(bikeBooking);
            bike.setBikeLocked(true);
            this.bikeRepo.save(bike);

        } else {
            throw new BadBikeException("Bike is alredy booked", "bike");
        }
        BookingRecords bookingRecords=new BookingRecords();
        bookingRecords.setBike(this.objectMappingService.entityToPojo(bike, BikeforBookingReturnDto.class));

        bookingRecords.setBookedprice(amountNeedToPay);
        bookingRecords.setBookingDate(bookDto.getBookingDate());
        bookingRecords.setEndBookingDate(bookDto.getEndBookingDate());
        bookingRecords.setKmDriven(null);
        bookingRecords.setPricePaid(null);
        bookingRecords.setBookingId(bikeBookingSaved.getBookingId());
        bookingRecords.setPickUpLocation(this.objectMappingService.entityToPojo(bike.getBikeProviderPartner().getCurrentAddress(),CurrentAddressDto.class));
        return bookingRecords;

    }

    public BikeEstimadePaymentBeforeBooked paymentDetails(UUID bikeId, UUID applicationId, BookDto bookDto) {

        ApplicationUser applicationUser = this.applicationUserRepo.findById(applicationId).orElseThrow(() -> new ReviewException("User not exist with this id" + applicationId, "ApplicationUser"));
        Bike bike = this.bikeRepo.findById(bikeId).orElseThrow(() -> new BadBikeException("Bike is not present with " + bikeId, "BIKE"));
        if (bike.getBikeProviderPartner().getIsAvilable() == true && bike.getUnder_Maintenance().equals(false))
    {

            if (bookDto == null) {
                throw new BadBikeException("Inpute is not vaild", "input");
            }

        Bike bikeAvibility= this.bikeFindingService.parseBikeForAvailability(bike,bookDto.getBookingDate(),bookDto.getEndBookingDate());
        if(bikeAvibility==null && bike.getBikeLocked()){
            throw new BookingException("Bike is not avilable for this date", "Bike");
        }
            Double pricePerDay = bike.getPricePerDay();

            HashMap<String, Long> timing = TimeCalculation(bookDto.getBookingDate(), bookDto.getEndBookingDate());
                Double amountNeedToPay= priceNeedToPay(timing,pricePerDay);

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


    public HashMap<java.lang.String, Long> TimeCalculation(LocalDateTime bookedDay, LocalDateTime lastdate) {
        Duration duration = Duration.between(bookedDay, lastdate);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        HashMap<String, Long> time = new HashMap<>();
        time.put("days", days);
        time.put("hours", hours);
        return time;
    }
    public  Double priceNeedToPay(HashMap<String,Long>timing,Double pricePerDay) {
        long days = timing.get("days");
        long hour = timing.get("hours");
        if (days <zeroDay  && hour < zeroHour) {
            throw new DateTimeException("Please select proper date ");
        }
        if (hour > zeroHour && days == zeroDay) {
            days = 1;
        }
        if (hour > 8) {
            days = days + 1;
        }
        Double amountNeedToPay = days * pricePerDay;

        return amountNeedToPay;
    }

    public BookingRecords getBookingRecordsById(UUID bookingid){
        BikeBooking booking=this.bikeBookingJpa.findById(bookingid).orElseThrow(()-> new BookingException("Booking is not found with this id","Booking Id"));
        List<Bike>bikes =booking.getBikesBookReg();
         Bike bike  =bikes.get(0);
         List<ApplicationUser> user=booking.getApplicationUserBikeBook();
             ApplicationUser applicationUser = user.get(0);
             BookingRecords bookingRecords=new BookingRecords();
             bookingRecords.setBike(this.objectMappingService.entityToPojo(bike, BikeforBookingReturnDto.class));
             bookingRecords.setBookedprice(booking.getBookedprice());
             bookingRecords.setBookingDate(booking.getDateBook());
             bookingRecords.setBookingId(booking.getBookingId());
             bookingRecords.setEndBookingDate(booking.getTillDate());
             bookingRecords.setKmDriven(booking.getKmDriven());
             bookingRecords.setLicencePlate(bike.getLicencePlate());
             bookingRecords.setPickUpLocation(this.objectMappingService.entityToPojo(bike.getBikeProviderPartner().getCurrentAddress(),CurrentAddressDto.class));
             bookingRecords.setPricePaid(booking.getPricePaid());
             return bookingRecords;
    }


    }

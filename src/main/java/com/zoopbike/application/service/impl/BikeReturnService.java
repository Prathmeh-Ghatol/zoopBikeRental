package com.zoopbike.application.service.impl;

import com.zoopbike.application.dto.BikeReturnBillingDto;
import com.zoopbike.application.dto.BikeReturnDetailsDto;
import com.zoopbike.application.dto.BikeReturnDto;
import com.zoopbike.application.dto.BookingRecords;
import com.zoopbike.application.entity.ApplicationUser;
import com.zoopbike.application.entity.Bike;
import com.zoopbike.application.entity.BikeBooking;
import com.zoopbike.application.exception.ReviewException;
import com.zoopbike.application.exception.BikeReturnException;
import com.zoopbike.application.exception.BookingException;
import com.zoopbike.application.repo.ApplicationUserRepo;
import com.zoopbike.application.repo.BikeBookingJpa;
import com.zoopbike.application.repo.BikeRepo;
import com.zoopbike.application.utils.ObjectMappingService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class BikeReturnService {
    private ApplicationUserRepo applicationUserRepo;
    private BikeRepo bikeRepo;
    private BikeBookingJpa bikeBookingJpa;
    private Double priceAfterFreeKm;
    private Double priceTimeBooked;
    private Double priceForDay;

    private Double meterReadingDiff;

    private ObjectMappingService mappingService;

    private ApplicationUserserviceImpl applicationUserService;
    private Double bikePriceByKm;

    private BookingService bookingService;



        BikeReturnService(ApplicationUserRepo applicationUserRepo, BikeRepo bikeRepo, BikeBookingJpa bikeBookingJpa,
                          ApplicationUserserviceImpl applicationUserservice,BookingService bookingService,ObjectMappingService mappingService) {
        this.bikeRepo = bikeRepo;
        this.applicationUserRepo = applicationUserRepo;
        this.bikeBookingJpa = bikeBookingJpa;
        this.bookingService = bookingService;
        this.applicationUserService=applicationUserservice;
        this.mappingService=mappingService;
    }

public BikeReturnBillingDto returnBikeBooking(UUID bookingId, UUID applicationUserId, BikeReturnDetailsDto bikeReturnDetailsDto) {
        ApplicationUser applicationUser = this.applicationUserRepo.
                findById(applicationUserId).orElseThrow(() -> new ReviewException("Application User Not Found" + applicationUserId.toString(), "ApplicationUser"));
        Set<BookingRecords> bikeBoooking = applicationUserService.getCurrentBookingOfuser(applicationUserId);
        Boolean bookingFound=false;
        System.out.println(bikeBoooking);
        BikeBooking booking = bikeBookingJpa.findById(bookingId).orElseThrow(() -> new BookingException("Booking is not avilable" +
                bookingId.toString(), " Booking"));
        for(BookingRecords bookingRC:bikeBoooking){
            if(bookingRC.getBookingId()==booking.getBookingId()){
                bookingFound=true;
            }
        }
            BikeReturnBillingDto bikeReturnBillingDto = new BikeReturnBillingDto();
        System.out.println(bookingFound);
        if (bookingFound && booking.getBooking_Cancelled().equals(false)) {
            List<Bike> bikeALl = booking.getBikesBookReg();
            Bike bike=bikeALl.get(0);
            System.out.println(bikeALl);
            priceAfterFreeKm = bike.getAfterfreeDriveKmChargePerKm();
            priceTimeBooked = booking.getPricePaid();
            priceForDay = bike.getPricePerDay();
            if (bike.getCurrentMeterReading() < bikeReturnDetailsDto.getAfterReturnBikeMeterReading()) {
                meterReadingDiff = bikeReturnDetailsDto.getAfterReturnBikeMeterReading() - bike.getCurrentMeterReading();
                System.out.println(meterReadingDiff);
                if (meterReadingDiff < 0) {
                    throw new BookingException("Meter Reading never will negtive ", "METER READING");
                }
//                       if(booking.getTillDate().isBefore(bikeReturnDetailsDto.getReturnDate()) ||
//                               booking.getTillDate().isEqual(bikeReturnDetailsDto.getReturnDate())&&
//                               bike.getFreeDriveKm()>=meterReadingDiff){
//                           bikeReturnBillingDto.setPriceToPay(booking.getPricePaid());
//                       }
                Double priceNeedToPay = 0.0;

                HashMap<String, Long> timing = bookingService.TimeCalculation(booking.getDateBook(), bikeReturnDetailsDto.getReturnDate());
                Double priceOnDay = this.bookingService.priceNeedToPay(timing, bike.getPricePerDay());
                if (bike.getFreeDriveKm() < meterReadingDiff) {
                    Double payForKm=meterReadingDiff-bike.getFreeDriveKm();
                    priceNeedToPay = priceOnDay + payForKm * bike.getAfterfreeDriveKmChargePerKm();
                System.out.println( " system" +priceNeedToPay);
                }else {
                    priceNeedToPay = priceOnDay;
                }
                bikeReturnBillingDto.setPriceToPay(priceNeedToPay);
                bikeReturnBillingDto.setReturnDateAsPerBooking(booking.getTillDate());
                bikeReturnBillingDto.setAfterReturnBikeMeterReading(bikeReturnDetailsDto.getAfterReturnBikeMeterReading());
                bikeReturnBillingDto.setBookingBikeMeterReading(bike.getCurrentMeterReading());
                bikeReturnBillingDto.setBikeReturnDto(mappingService.entityToPojo(bike, BikeReturnDto.class));
                bikeReturnBillingDto.setBookingId(booking.getBookingId());
                bikeReturnBillingDto.setReturnDate(bikeReturnDetailsDto.getReturnDate());
                bikeReturnBillingDto.setBookTimePrice(booking.getBookedprice());
                bikeReturnBillingDto.setFreeKm(Double.valueOf(bike.getFreeDriveKm()));
                bikeReturnBillingDto.setDrivenKm(meterReadingDiff);
                bikeReturnBillingDto.setBookedDate(booking.getDateBook());
                bike.setCurrentMeterReading(bikeReturnDetailsDto.getAfterReturnBikeMeterReading());
                bike.setBikeLocked(false);
                this.bikeRepo.save(bike);
                booking.setPricePaid(priceNeedToPay);
                booking.setKmDriven(meterReadingDiff);
                booking.setActualReturnDate(bikeReturnDetailsDto.getReturnDate());
                this.bikeBookingJpa.save(booking);
                bikeReturnBillingDto.setAfterReturnBikeMeterReading(bikeReturnDetailsDto.getAfterReturnBikeMeterReading());
                return bikeReturnBillingDto;
            }
            throw new BikeReturnException("Bike Already Return","bike");
        }
        else {
            throw new BikeReturnException("Your Booking is not present","BikeBooking");

        }


}
}

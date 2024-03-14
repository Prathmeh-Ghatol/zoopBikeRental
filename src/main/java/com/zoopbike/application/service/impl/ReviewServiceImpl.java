package com.zoopbike.application.service.impl;

import com.zoopbike.application.dto.BikeReturnDetailsDto;
import com.zoopbike.application.dto.BikeReturnDto;
import com.zoopbike.application.dto.ReviewDto;
import com.zoopbike.application.entity.ApplicationUser;
import com.zoopbike.application.entity.Bike;
import com.zoopbike.application.entity.BikeBooking;
import com.zoopbike.application.entity.Review;
import com.zoopbike.application.exception.ApplicationUserException;
import com.zoopbike.application.exception.BadBikeException;
import com.zoopbike.application.exception.BookingException;
import com.zoopbike.application.exception.ReviewException;
import com.zoopbike.application.repo.ApplicationUserRepo;
import com.zoopbike.application.repo.BikeBookingJpa;
import com.zoopbike.application.repo.ReviewRepo;
import com.zoopbike.application.utils.ObjectMappingService;
import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewServiceImpl {


    private ApplicationUserRepo applicationUserRepo;
    private BikeBookingJpa bikeBookingJpa;
    private ReviewRepo reviewRepo;

    private ObjectMappingService objectMappingService;
    @Autowired
    public ReviewServiceImpl(ApplicationUserRepo applicationUserRepo, BikeBookingJpa bikeBookingJpa,
                             ReviewRepo reviewRepo,ObjectMappingService objectMappingService){
        this.applicationUserRepo=applicationUserRepo;
        this.bikeBookingJpa=bikeBookingJpa;
        this.reviewRepo=reviewRepo;
        this.objectMappingService=objectMappingService;
    }

    public ReviewDto postReview(UUID applicationUserId, UUID BookingId, ReviewDto reviewDto){
        ApplicationUser applicationUser =this.applicationUserRepo.findById(applicationUserId).orElseThrow(()->
                    new ApplicationUserException( "User not found with id", "applicationUser"));

        BikeBooking booking=this.bikeBookingJpa.findById(BookingId).orElseThrow(()-> new
                BookingException("Booking is not aviliable", BookingId.toString()));

          List<BikeBooking>bookingsOfApplication =applicationUser.getBooking();

          Boolean bookingIsPresent=bookingsOfApplication.contains(booking);
          if(bookingIsPresent==true){
             Bike bike= booking.getBikesBookReg().get(0);
             if(bike==null){
                 throw new BadBikeException("Bike is not present with booking","Bike");
             }
             Review SavedReview;
              synchronized (this){
                  Review review=new Review();
                  review.setApplicationUser(applicationUser);
                  review.setBike(bike);
                  review.setComment(reviewDto.getComment());
                  SavedReview=this.reviewRepo.save(review);
              }
              ReviewDto reviewDtoSaved=new ReviewDto();
              reviewDtoSaved.setApplicationName(SavedReview.getApplicationUser().getApplication_Username());
              reviewDtoSaved.setBikeReturnDetailsDto(this.objectMappingService.entityToPojo(SavedReview.getBike(), BikeReturnDetailsDto.class));
              reviewDtoSaved.setComment(SavedReview.getComment());
              return reviewDtoSaved;

          }else {
              throw new BookingException("Sorry Booking is not belong to you", "Booking");
          }

    }

}

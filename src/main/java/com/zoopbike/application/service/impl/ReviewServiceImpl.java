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
import com.zoopbike.application.repo.BikeRepo;
import com.zoopbike.application.repo.ReviewRepo;
import com.zoopbike.application.utils.ObjectMappingService;
import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl {


    private ApplicationUserRepo applicationUserRepo;
    private BikeBookingJpa bikeBookingJpa;
    private ReviewRepo reviewRepo;

    private ObjectMappingService objectMappingService;

    private BikeRepo bikeRepo;

    @Autowired
    public ReviewServiceImpl(ApplicationUserRepo applicationUserRepo, BikeBookingJpa bikeBookingJpa,
                             ReviewRepo reviewRepo, ObjectMappingService objectMappingService,
                             BikeRepo bikeRepo) {
        this.applicationUserRepo = applicationUserRepo;
        this.bikeBookingJpa = bikeBookingJpa;
        this.reviewRepo = reviewRepo;
        this.objectMappingService = objectMappingService;
        this.bikeRepo=bikeRepo;
    }

    public ReviewDto postReview(UUID applicationUserId, UUID BookingId, ReviewDto reviewDto) {
        ApplicationUser applicationUser = this.applicationUserRepo.findById(applicationUserId).orElseThrow(() ->
                new ApplicationUserException("User not found with id", "applicationUser"));

        BikeBooking booking = this.bikeBookingJpa.findById(BookingId).orElseThrow(() -> new
                BookingException("Booking is not aviliable", BookingId.toString()));

        List<BikeBooking> bookingsOfApplication = applicationUser.getBooking();

        Boolean bookingIsPresent = bookingsOfApplication.contains(booking);
        if (bookingIsPresent == true) {
            Bike bike = booking.getBikesBookReg().get(0);
            if (bike == null) {
                throw new BadBikeException("Bike is not present with booking", "Bike");
            }
            Review SavedReview;
            synchronized (this) {
                Review review = new Review();
                review.setApplicationUser(applicationUser);
                review.setBike(bike);
                review.setCommentRealtedToBike(reviewDto.getCommentRealtedToBike());
                review.setCommentRealtedBikeProvider(reviewDto.getCommentRealtedBikeProvider());
                review.setTime(LocalDateTime.now());
                SavedReview = this.reviewRepo.save(review);
            }
            ReviewDto reviewDtoSaved = new ReviewDto();
            reviewDtoSaved.setApplicationName(SavedReview.getApplicationUser().getApplication_Username());
              reviewDtoSaved.setCommentRealtedBikeProvider(SavedReview.getCommentRealtedBikeProvider());
            reviewDtoSaved.setCommentRealtedToBike(SavedReview.getCommentRealtedToBike());
            reviewDtoSaved.setReviewId(SavedReview.getReviewId());
            reviewDtoSaved.setTime(SavedReview.getTime());
            return reviewDtoSaved;

        } else {
            throw new BookingException("Sorry Booking is not belong to you", "Booking");
        }

    }
    public List<ReviewDto>getAllReviewBike(UUID bookeId){
        Bike bike=this.bikeRepo.findById(bookeId).orElseThrow(
                ()-> new BadBikeException("Bike is not found", bookeId.toString()));
          List<Review>allReview=  bike.getReview();
          List<ReviewDto> all=allReview.stream().
                  map( reivew -> objectMappingService.entityToPojo(reivew,ReviewDto.class)).collect(Collectors.toList());
          return all;
    }
    public ReviewDto updateBooking(UUID reviewId,ReviewDto reviewDto){
        Optional<Review> review=reviewRepo.findById(reviewId);
        if(review.isEmpty()){
            throw new ReviewException("review is not found with", reviewId.toString());
        }
        Review updateReview=review.get();
        updateReview.setCommentRealtedBikeProvider(reviewDto.getCommentRealtedBikeProvider());
        updateReview.setCommentRealtedToBike(reviewDto.getCommentRealtedToBike());
        updateReview.setUpdatedCommentTime(LocalDateTime.now());
        Review reviewUpdate=this.reviewRepo.save(updateReview);
        ReviewDto reviewDtoSaved= objectMappingService.entityToPojo(reviewUpdate, ReviewDto.class);
        reviewDtoSaved.setApplicationName(updateReview.getApplicationUser().getApplication_Username());
        return reviewDtoSaved;

    }

    public Boolean deleteReview(UUID reviewId){
        Review review=this.reviewRepo.findById(reviewId).orElseThrow(()->new ReviewException("Review not found", reviewId.toString()));
        this.reviewRepo.deleteById(review.getReviewId());
        return reviewRepo.existsById(reviewId);
    }

}

package com.zoopbike.application.controller;

import com.zoopbike.application.dto.ReviewDto;
import com.zoopbike.application.service.impl.ReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/review")
public class ReviewController {
    @Autowired
    private ReviewServiceImpl reviewService;

    @PostMapping(value = "/post/review/application/{applicationId}/booking/{bookingId}")
    @PreAuthorize("hasRole('ROLE_APPLICATION_USER')")
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto, @PathVariable("applicationId") UUID applicationUserID,
                                                  @PathVariable("bookingId") UUID bookingId) {
        return ResponseEntity.ok(this.reviewService.postReview(applicationUserID, bookingId, reviewDto));

    }

    @PostMapping(value = "/get/all/review/{bikeId}")
    @PreAuthorize("hasRole('ROLE_APPLICATION_USER') and hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReviewDto>> createReview(@PathVariable("bikeId") UUID bookingId) {
        return ResponseEntity.ok(this.reviewService.getAllReviewBike(bookingId));
    }

    @PutMapping(value = "/update/review/{review}")
    @PreAuthorize("hasRole('ROLE_APPLICATION_USER')")
    public ResponseEntity<ReviewDto> update(@RequestBody ReviewDto reviewDto, @PathVariable("review") UUID review) {
        return ResponseEntity.ok(this.reviewService.updateBooking(review, reviewDto));

    }

    @DeleteMapping(value = "/delete/review/{uuid}")
    @PreAuthorize("hasRole('ROLE_APPLICATION_USER') and hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map> delete(@PathVariable("uuid") UUID reviewId) {
        Boolean deleteById = this.reviewService.deleteReview(reviewId);
        Map<String, Boolean> deleteReview = new HashMap<>();
        deleteReview.put("Deleted comment ", deleteById);
        return ResponseEntity.ok(deleteReview);

    }


}

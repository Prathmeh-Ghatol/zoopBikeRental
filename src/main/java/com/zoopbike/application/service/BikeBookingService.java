package com.zoopbike.application.service;

import com.zoopbike.application.dto.BookDto;
import com.zoopbike.application.dto.BookingRecords;
import com.zoopbike.application.entity.BikeBooking;

import java.util.UUID;

public interface BikeBookingService {


    BookingRecords bookingBike(UUID bikeId, UUID applicationId, BookDto bookDto);
}

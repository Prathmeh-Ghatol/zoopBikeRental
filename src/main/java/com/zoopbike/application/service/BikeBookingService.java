package com.zoopbike.application.service;

import com.zoopbike.application.dto.BookDto;
import com.zoopbike.application.entity.BikeBooking;

import java.util.UUID;

public interface BikeBookingService {


    BikeBooking bookingBike(UUID bikeId, UUID applicationId, BookDto bookDto);
}

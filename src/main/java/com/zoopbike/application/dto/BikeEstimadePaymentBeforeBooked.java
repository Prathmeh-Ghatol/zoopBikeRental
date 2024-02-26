package com.zoopbike.application.dto;
import com.zoopbike.application.entity.Bike;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BikeEstimadePaymentBeforeBooked {
    private   LocalDateTime bookingDate;
    private LocalDateTime endBookingDate;
    private BikeReturnDto bike;
    private CurrentAddressDto pickupLoLocation;
    private Double price;

}

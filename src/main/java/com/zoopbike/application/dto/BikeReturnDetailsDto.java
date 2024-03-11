package com.zoopbike.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BikeReturnDetailsDto {
    private Double afterReturnBikeMeterReading;
    private LocalDateTime returnDate;


}

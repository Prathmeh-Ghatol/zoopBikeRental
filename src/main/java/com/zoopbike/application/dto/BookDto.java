package com.zoopbike.application.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookDto {

    LocalDateTime bookingDate;

    LocalDateTime endBookingDate;
}

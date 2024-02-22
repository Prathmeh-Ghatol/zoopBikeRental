//package com.zoopbike.application.entity;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.data.convert.Jsr310Converters;
//
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class BikeBooking {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID bookingRecordId;
//
//    @ManyToMany
//    private Set<ApplicationUser> applicationUser;
//
//    @ManyToMany
//    private Set<Bike>bookedBike=new HashSet<>();
//
//    @Column(name = "start_time")
//    private LocalDateTime bookedDateTime;
//
//    @Column(name = "end_time")
//    private LocalDateTime expected_realised_DateTime;
//
//    @Column(name = "Bike_Provider_Id")
//    private UUID bikeProviderId;
//
//}

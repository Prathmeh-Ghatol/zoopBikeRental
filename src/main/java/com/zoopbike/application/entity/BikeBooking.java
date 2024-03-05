package com.zoopbike.application.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.zoopbike.application.utils.BIkeBookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class BikeBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookingId;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateBook;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime tillDate;

    @ManyToMany
    @JoinTable(name = "BIKE_USER_BOOK",joinColumns = {@JoinColumn(name = "BOOKING_ID",referencedColumnName = "bookingId")},
            inverseJoinColumns = {@JoinColumn(name= "APPLICATION_USER_ID",referencedColumnName = "applicationUserId")}
    )
    @JsonBackReference
    //@JsonManagedReference
    private List<ApplicationUser> applicationUserBikeBook;


    @ManyToMany
        @JoinTable(name = "`bikesBooked`",joinColumns = {@JoinColumn(name = "BOOKING_ID",referencedColumnName = "bookingId")},
            inverseJoinColumns = {@JoinColumn(name= "BIKE_ID",referencedColumnName = "bikeId")}
    )
    @JsonBackReference
    //@JsonManagedReference
    private List<Bike> bikesBookReg;

    private Double Bookedprice;

    private Double pricePaid;

    private Double kmDriven;




}
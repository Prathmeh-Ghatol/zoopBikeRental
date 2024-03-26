package com.zoopbike.application.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.w3c.dom.DOMStringList;

import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "APPLICATION_USER")
public class ApplicationUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID applicationUserId;

    private String application_Username ;


    @Column(name = "Application_user_email",nullable = false,unique = true)
    private String email;
    @Column(name = "Application_user_cellnumber",nullable = false,unique = true)
    private String cellNumber;
    private String Password;
    @OneToOne(cascade = CascadeType.ALL)
    private CurrentAddress currentAddress;

    @OneToOne(cascade = CascadeType.ALL)
    private Permanentaddress permanentaddress;

    @ManyToMany(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY,mappedBy = "applicationUserBikeBook")
   @JsonManagedReference
   // @JsonBackReference
    private List<BikeBooking>Booking;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "applicationUser")
    private  List<Review>reviews;

    private  String profileImage;

    private String adhar;

    private String drivingLicence;


    private List<String> roles;

    @Column(name = "locked")
    private Boolean locked;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority>authorities=new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;

    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

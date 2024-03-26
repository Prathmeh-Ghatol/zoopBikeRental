package com.zoopbike.application.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.domain.Range;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BIKE_PROVIDER")
public class BikeProviderPartner implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bikeProviderPartnerId;

    @Column(name = "Name")
    private String name;

    @Column(unique = true ,nullable = false)
    private String email;

    @Column(unique = true)
    private String cellNumber;

    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private Permanentaddress permanentaddress;

    @OneToOne(cascade = CascadeType.ALL)
    private CurrentAddress currentAddress;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "bikeProviderPartner")
    private Set<Bike> bikeOwner=new HashSet<>();

    private Boolean isAvilable;

    private  String profileImage;

    private String adhar;

    private String drivingLicence;

    @Column(name = "ROLES")
    private List<String>roles;

    @Column
    private Boolean locked;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority>authoritySet = new HashSet<>();
        for(String role:roles){
            authoritySet.add(new SimpleGrantedAuthority(role));
        }
    return authoritySet;
    }

    @Override
    public String getUsername() {
        return email ;
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

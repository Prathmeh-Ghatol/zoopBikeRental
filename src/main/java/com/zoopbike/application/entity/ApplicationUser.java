package com.zoopbike.application.entity;

import jakarta.persistence.*;
import lombok.*;
import org.w3c.dom.DOMStringList;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "APPLICATION_USER")
public class ApplicationUser {
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



}

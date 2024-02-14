package com.zoopbike.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PermenetAddressDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID permanentAddressId;
    private Long pincode;
    private String city;
    private String state;
    private String address;
    private String landmark;
}

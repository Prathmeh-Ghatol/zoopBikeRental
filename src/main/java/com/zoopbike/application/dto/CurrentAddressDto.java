package com.zoopbike.application.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CurrentAddressDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID currentAddressId;

    private Long pincode;

    private String city;

    private String state;

    private String address;

    private String landmark;
}

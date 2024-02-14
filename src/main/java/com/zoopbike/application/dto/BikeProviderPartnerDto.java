package com.zoopbike.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BikeProviderPartnerDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID bikeProviderPartnerId;
    private String name;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    private String email;
    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String cellNumber;
    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    private String password;
    @NotBlank(message = "Please Eneter your perment address")
    private PermenetAddressDto permenetAddressDto;
    private CurrentAddressDto CurrentAddressDto;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean currentAddressSameToPermentAddress;
}

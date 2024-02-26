package com.zoopbike.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zoopbike.application.entity.Bike;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Set;
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
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",message = "\"please enter proper email")
    private String email;
    @NotBlank
   // @Pattern(regexp = "^[789]\\d{9}$\n",message = "please enter phone number")
    private String cellNumber;
    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Your password must meet the following criteria to be considered strong:\n" +
            "\n" +
            "Include a mix of upper and lowercase letters.\n" +
            "Include at least one digit (0-9).\n" +
            "Include at least one special character from the set [@#$%^&+=].\n" +
            "Be at least 8 characters long.\n" +
            "Avoid using any whitespace characters.\n" +
            "Example of a strong password: MyPassword@123\n" +
            "\n" +
            "Please ensure that your password meets all of the above requirements to ensure security.")
    private String password;

    private PermenetAddressDto permenetAddressDto;
    private CurrentAddressDto CurrentAddressDto;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean currentAddressSameToPermentAddress;
    private Boolean isAvilable;


}

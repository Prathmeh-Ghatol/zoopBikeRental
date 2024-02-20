package com.zoopbike.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY )
    private UUID applicationUserId;

    @NotBlank
    private String application_Username ;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    private String email;

    private String cellNumber;

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
    private String Password;

    private  CurrentAddressDto currentAddressDto;
    private PermenetAddressDto permenetAddressDto;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean currentAddressSameToPermentAddress;
}





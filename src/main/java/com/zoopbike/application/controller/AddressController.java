package com.zoopbike.application.controller;

import com.zoopbike.application.dto.CurrentAddressDto;
import com.zoopbike.application.dto.PermenetAddressDto;
import com.zoopbike.application.entity.CurrentAddress;
import com.zoopbike.application.service.impl.AddressServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/address/update/")
public class AddressController {
    @Autowired
    AddressServiceImpl addressService;
    @PutMapping(value = "bikeProviderPartner/currentAddress/{email}")
    @PreAuthorize("hasRole('ROLE_APPLICATION_USER') and hasRole('ROLE_BIKEPROVIDER_USER')")
    public ResponseEntity <CurrentAddressDto> updateCurrentAddressOfBikeProvider(@RequestBody  CurrentAddressDto currentAddressDto,
                                                                                 @PathVariable("email") String email){
       CurrentAddressDto currentAddress =addressService.updateCurrentAddressForBikeProviderPartner(currentAddressDto, email);
        return ResponseEntity.status(HttpStatus.OK).body(currentAddress);
    }
    @PutMapping(value = "bikeProviderPartner/permentAddress/{email}")
    @PreAuthorize("hasRole('ROLE_APPLICATION_USER') and hasRole('ROLE_BIKEPROVIDER_USER')")
    public ResponseEntity <PermenetAddressDto>updateCurrentAddressOfBikeProvider(@RequestBody PermenetAddressDto permenetAddressDto,
                                                                                 @PathVariable("email") String email){
        PermenetAddressDto permenetAddressDto1 =addressService.updatepermentAddressForBikeProviderPartner(permenetAddressDto, email);
        return ResponseEntity.status(HttpStatus.OK).body(permenetAddressDto1);
    }

    @PutMapping(value = "applicationuser/currentAddress/{email}")
    @PreAuthorize("hasRole('ROLE_APPLICATION_USER') and hasRole('ROLE_BIKEPROVIDER_USER')")
    public ResponseEntity <CurrentAddressDto> updateCurrentAddressOfApplicationUser(@RequestBody  CurrentAddressDto currentAddressDto,
                                                                                 @PathVariable("email") String email){
        CurrentAddressDto currentAddress =addressService.updatecurrentAddressForApplicationUser(currentAddressDto, email);
        return ResponseEntity.status(HttpStatus.OK).body(currentAddress);
    }
    @PutMapping(value = "applicationuser/permentAddress/{email}")
    @PreAuthorize("hasRole('ROLE_APPLICATION_USER') and hasRole('ROLE_BIKEPROVIDER_USER')")
    public ResponseEntity <PermenetAddressDto>updatePermentAddressForApplicationUser(@RequestBody PermenetAddressDto permenetAddressDto,
                                                                                 @PathVariable("email") String email){
        PermenetAddressDto permenetAddressDto1 =addressService.updatepermanentAddressForApplicationUser(permenetAddressDto, email);
        return ResponseEntity.status(HttpStatus.OK).body(permenetAddressDto1);
    }



}

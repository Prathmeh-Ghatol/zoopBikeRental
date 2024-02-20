package com.zoopbike.application.service;

import com.zoopbike.application.dto.CurrentAddressDto;
import com.zoopbike.application.dto.PermenetAddressDto;

public interface AddressService {

    public CurrentAddressDto updateCurrentAddressForBikeProviderPartner(CurrentAddressDto currentAddressDto,String email);
    public PermenetAddressDto updatepermentAddressForBikeProviderPartner(PermenetAddressDto permenetAddressDto, String email) ;
    PermenetAddressDto updatepermanentAddressForApplicationUser(PermenetAddressDto permenetAddressDto, String email);
    public CurrentAddressDto updatecurrentAddressForApplicationUser(CurrentAddressDto currentAddressDto, String email);

    }


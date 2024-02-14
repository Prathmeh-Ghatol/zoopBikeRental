package com.zoopbike.application.service;

import com.zoopbike.application.dto.BikeProviderPartnerDto;

import java.util.UUID;

public interface BikePartnerService {

    public BikeProviderPartnerDto register(BikeProviderPartnerDto bikeProviderPartnerDto);

    public Boolean deregister(UUID id);
}

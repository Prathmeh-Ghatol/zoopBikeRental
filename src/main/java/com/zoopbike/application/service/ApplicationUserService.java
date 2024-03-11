package com.zoopbike.application.service;

import com.zoopbike.application.dto.ApplicationUserDto;

import java.util.UUID;

public interface ApplicationUserService {

    public ApplicationUserDto registerApplicationUser(ApplicationUserDto applicationUserDto);
    public ApplicationUserDto update(String email,  ApplicationUserDto applicationUserDto);
    public Boolean deRegisterApplicationUser(String email);
    public ApplicationUserDto getApplicationUserByEmailorId(String email, UUID uuid);

    }

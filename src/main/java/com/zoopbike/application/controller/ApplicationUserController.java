package com.zoopbike.application.controller;


import com.zoopbike.application.dto.ApplicationUserDto;
import com.zoopbike.application.dto.GenricPage;
import com.zoopbike.application.entity.ApplicationUser;
import com.zoopbike.application.service.ApplicationUserService;
import com.zoopbike.application.service.impl.ApplicationUserserviceImpl;
import com.zoopbike.application.utils.zoopBikeRentalApplicationConstant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/application/user")
public class ApplicationUserController {

    @Autowired
    ApplicationUserserviceImpl applicationUserService;
    @Autowired
    zoopBikeRentalApplicationConstant zoopBikeRentalApplicationConstant;

    @PostMapping(value = "/register")
    ResponseEntity<ApplicationUserDto> register(@RequestBody @Valid ApplicationUserDto applicationUserDto) {
        ApplicationUserDto applicationUser = this.applicationUserService.registerApplicationUser(applicationUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationUser);
    }

    @PutMapping(value = "/update/{email}")
    ResponseEntity<ApplicationUserDto> update(@PathVariable("email") String email,
                                              @Valid  @RequestBody ApplicationUserDto applicationUserDto) {
        ApplicationUserDto applicationUser = this.applicationUserService.update(email, applicationUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationUser);
    }

    @DeleteMapping(value = "/delete/{email}")
    ResponseEntity<Map<String, Boolean>> delete(@PathVariable("email") String email) {
        Boolean deleteConfirmFlag = this.applicationUserService.deRegisterApplicationUser(email);
        Map<String, Boolean> responceMap = new HashMap<>();
        responceMap.put("Application user account removed" + email, deleteConfirmFlag);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responceMap);
    }

    @GetMapping(value = "/get/email/{email}")
    ResponseEntity<ApplicationUserDto> get(@PathVariable("email") String email) {
        ApplicationUserDto applicationUser = this.applicationUserService.getApplicationUserByEmailorId(email, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationUser);
    }

    @GetMapping(value = "/get/id/{uuid}")
    ResponseEntity<ApplicationUserDto> getById(@PathVariable("uuid") UUID uuid) {
        ApplicationUserDto applicationUser = this.applicationUserService.getApplicationUserByEmailorId(null, uuid);
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationUser);
    }

    @GetMapping(value = "/get/all")
    ResponseEntity<GenricPage<ApplicationUserDto>> getAllApplicationUser(
            @RequestParam(value = "pageNo", defaultValue = com.zoopbike.application.utils.zoopBikeRentalApplicationConstant.defualtApplicationPageNO, required = false) int PageNo,
            @RequestParam(value = "pageSize", defaultValue = com.zoopbike.application.utils.zoopBikeRentalApplicationConstant.defaultApplicationPageSize, required = false) int PageSize) {
        GenricPage<ApplicationUserDto> allUser = this.applicationUserService.getAllUser(PageNo, PageSize);
        return ResponseEntity.status(HttpStatus.OK).body(allUser);

    }





}

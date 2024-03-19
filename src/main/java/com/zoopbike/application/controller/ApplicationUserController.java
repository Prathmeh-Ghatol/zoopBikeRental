package com.zoopbike.application.controller;


import com.zoopbike.application.dto.ApplicationUserDto;
import com.zoopbike.application.dto.BookingRecords;
import com.zoopbike.application.dto.GenricPage;
import com.zoopbike.application.entity.ApplicationUser;
import com.zoopbike.application.entity.BikeBooking;
import com.zoopbike.application.repo.BikeBookingJpa;
import com.zoopbike.application.service.ApplicationUserService;
import com.zoopbike.application.service.impl.ApplicationUserserviceImpl;
import com.zoopbike.application.service.impl.S3ServiceImpl;
import com.zoopbike.application.service.impl.S3service;
import com.zoopbike.application.utils.zoopBikeRentalApplicationConstant;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = "/application/user")
public class ApplicationUserController {

    @Autowired
    private ApplicationUserserviceImpl applicationUserService;

    @Autowired
    private zoopBikeRentalApplicationConstant zoopBikeRentalApplicationConstant;

    @Autowired
    private S3ServiceImpl s3service;

    @Autowired
    private BikeBookingJpa bikeBookingJpa;

    @PostMapping(value = "/register")
    private ResponseEntity<ApplicationUserDto> register(@RequestBody @Valid ApplicationUserDto applicationUserDto) {
        ApplicationUserDto applicationUser = this.applicationUserService.registerApplicationUser(applicationUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationUser);
    }

    @PutMapping(value = "/update/{email}")
    ResponseEntity<ApplicationUserDto> update(@PathVariable("email") String email,
                                              @Valid @RequestBody ApplicationUserDto applicationUserDto) {
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


    @GetMapping(value = "/get/all/bookings/{userId}")
    public ResponseEntity<Set<BookingRecords>> getAllBookingofApplicationUser(@PathVariable("userId") UUID applicationUserId) {
        Set<BookingRecords> allBookingOfuser = applicationUserService.getAllBookingOfuser(applicationUserId);
        return ResponseEntity.status(HttpStatus.OK).body(allBookingOfuser);

    }

    @GetMapping(value = "/get/current/booking/{userId}")
    public ResponseEntity<Set<BookingRecords>> getCurrentBookingofApplicationUser(@PathVariable("userId") UUID applicationUserId) {
        Set<BookingRecords> allBookingOfuser = applicationUserService.getCurrentBookingOfuser(applicationUserId);
        return ResponseEntity.status(HttpStatus.OK).body(allBookingOfuser);

    }

    @PostMapping("/{userId}/image/upload")
    public ResponseEntity<String> uploadImage(@PathVariable("userId") UUID userId, @RequestParam("file") MultipartFile file,
                                              @RequestParam("imageType") String imageTypeToUpload) throws IOException {
        try {
            s3service.uploadImagesRelatedToUser(userId, file, imageTypeToUpload);
            return ResponseEntity.ok("Image uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }
    @PutMapping("/{userId}/image/update/upload")
    public ResponseEntity<String> updateImage(@PathVariable("userId") UUID userId, @RequestParam("file") MultipartFile file,
                                              @RequestParam("imageType") String imageTypeToUpload) throws IOException {
        try {
            s3service.updateProfileAndDocumentsOfApplicationUser(userId,  imageTypeToUpload,file);
            return ResponseEntity.ok("Image uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }

    @GetMapping("{userId}/image/download/profile/{profile}")
    public ResponseEntity<byte[]> downloadProfile(
            @PathVariable("userId") UUID userId,
            @PathVariable("profile") String docType)
    {        try {
            byte[] document = s3service.downloadProfileAndDocumentsOfApplicationUser(userId, docType);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(document, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("{userId}/image/download/Licence/{Licence}")
    public ResponseEntity<byte[]> downloadRtoLicence(
            @PathVariable("userId") UUID userId,
            @PathVariable("Licence") String docType)
    {        try {
            byte[] document = s3service.downloadProfileAndDocumentsOfApplicationUser(userId, docType);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(document, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("{userId}/image/download/Adhar/{Adhar}")
    public ResponseEntity<byte[]> downloadAdhar(
            @PathVariable("userId") UUID userId,
            @PathVariable("Adhar") String docType)
    {
        try {
            byte[] document = s3service.downloadProfileAndDocumentsOfApplicationUser(userId, docType);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(document, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping("{userId}/image/delete/Licence")
    public ResponseEntity<String> deleteLicence(@PathVariable("userId") UUID userId) {
        try {
            s3service.deleteProfileAndDocumentsOfApplicationUser(userId, "Rto_Licence");
            return ResponseEntity.ok("Document deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete document.");
        }
    }
    @DeleteMapping("{userId}/image/delete/Adhar")
    public ResponseEntity<String> deleteAdhar(@PathVariable("userId") UUID userId) {
        try {
            s3service.deleteProfileAndDocumentsOfApplicationUser(userId, "Aadhar");
            return ResponseEntity.ok("Document deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete document.");
        }
    }
    @DeleteMapping("{userId}/image/delete/Profile")
    public ResponseEntity<String> deleteProfile(@PathVariable("userId") UUID userId) {
        try {
            s3service.deleteProfileAndDocumentsOfApplicationUser(userId, "Profile");
            return ResponseEntity.ok("Document deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete document.");
        }
    }

    @PutMapping("/{applicationUserId}/update")
    public ResponseEntity<String> updateDocument(
            @PathVariable UUID applicationUserId,
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file) {
        try {
            s3service.updateProfileAndDocumentsOfApplicationUser(applicationUserId, documentType, file);
            return ResponseEntity.ok("Document updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update document.");
        }
    }


}

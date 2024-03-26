package com.zoopbike.application.controller;

import com.zoopbike.application.dto.BikeProviderPartnerDto;
import com.zoopbike.application.entity.BikeBooking;
import com.zoopbike.application.service.impl.BikePartnerServiceImpl;
import com.zoopbike.application.service.impl.S3ServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/bikepartner")
public class BikeProviderController {
    @Autowired
    S3ServiceImpl s3service;
    @Autowired
    BikePartnerServiceImpl bikePartnerService;
    @PostMapping(value = "/register")
    public ResponseEntity<BikeProviderPartnerDto>register(@Valid @RequestBody  BikeProviderPartnerDto bikeProviderPattnerDto){
     BikeProviderPartnerDto dto=   bikePartnerService.register(bikeProviderPattnerDto);
     return ResponseEntity.status(HttpStatus.CREATED).body(dto);

    }
    @DeleteMapping(value = "/deregister/{Id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_BIKEPROVIDER_USER')")
    public ResponseEntity<HashMap<Boolean,String>>deRegister(@PathVariable("Id") UUID id ){
        HashMap<Boolean,String>deleteObject=new HashMap<>();
        Boolean flag=this.bikePartnerService.deregister(id);
        deleteObject.put(flag, "BikePartner is removed sucssesfully" + id);
        return ResponseEntity.status(HttpStatus.OK).body( deleteObject);
    }
    @GetMapping(value = "/get/email/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_BIKEPROVIDER_USER') hasRole('ROLE_APPLICATION_USER')")
    public  ResponseEntity<BikeProviderPartnerDto>findBikeProviderPartner(@PathVariable("email") String email){
       BikeProviderPartnerDto bikeProviderPartnerDto= this.bikePartnerService.
               getBikeProviderPartnerByEmailorVenderId(email, null);
        return ResponseEntity.status(HttpStatus.OK).body(bikeProviderPartnerDto);
    }

    @PutMapping(value = "/update/email/{email}")

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_BIKEPROVIDER_USER')")
    public  ResponseEntity<String>updateBikeProviderPartner(@PathVariable("email") String email, @RequestBody BikeProviderPartnerDto bikeProviderPartnerDto){
        this.bikePartnerService.update(email, bikeProviderPartnerDto);
        return ResponseEntity.status(HttpStatus.OK).body("updated");
    }
//
//    @GetMapping(value = "/get/all/booking/{uuid}")
//    public  ResponseEntity<List<List<BikeBooking>>>get(@PathVariable("uuid") UUID id){
//      return ResponseEntity.ok( this.bikePartnerService.getBookedBikeStatus(id));
//    }
@PostMapping("/{bikeProviderId}/image/upload")

@PreAuthorize("hasRole('ROLE_BIKEPROVIDER_USER')")
public ResponseEntity<String> uploadImage(@PathVariable("bikeProviderId") UUID bikeProviderId, @RequestParam("file") MultipartFile file,
                                          @RequestParam("imageType") String imageTypeToUpload) throws IOException {
    try {
        s3service.uploadImagesRelatedToBikeProvider(bikeProviderId, file, imageTypeToUpload);
        return ResponseEntity.ok("Image uploaded successfully.");
    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
    }
}
    @PutMapping("/{bikeProviderId}/image/update/upload")
    @PreAuthorize("hasRole('ROLE_BIKEPROVIDER_USER')")
    public ResponseEntity<String> updateImage(@PathVariable("bikeProviderId") UUID bikeProviderId, @RequestParam("file") MultipartFile file,
                                              @RequestParam("imageType") String imageTypeToUpload) throws IOException {
        try {
            s3service.updateProfileAndDocumentsOfBikeProvider(bikeProviderId,  imageTypeToUpload,file);
            return ResponseEntity.ok("Image uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }

    @GetMapping("{bikeUserId}/image/download/profile/{profile}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_BIKEPROVIDER_USER') hasRole('ROLE_APPLICATION_USER')")
    public ResponseEntity<byte[]> downloadProfile(
            @PathVariable("bikeUserId") UUID bikeUserId,
            @PathVariable("profile") String docType)
    {        try {
        byte[] document = s3service.downloaProfileAndDocumentsOfBikeProvider(bikeUserId, docType);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(document, headers, HttpStatus.OK);
    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    }
    @GetMapping("{bikeUserId}/image/download/Licence/{Licence}")

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_BIKEPROVIDER_USER')")
    public ResponseEntity<byte[]> downloadRtoLicence(
            @PathVariable("bikeUserId") UUID userId,
            @PathVariable("Licence") String docType)
    {        try {
        byte[] document = s3service.downloaProfileAndDocumentsOfBikeProvider(userId, docType);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(document, headers, HttpStatus.OK);
    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    }
    @GetMapping(value = "{bikeUserId}/image/download/Adhar/{Adhar}")

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_BIKEPROVIDER_USER')")
    public ResponseEntity<byte[]> downloadAdhar(
            @PathVariable("bikeUserId") UUID userId,
            @PathVariable("Adhar") String docType)
    {
        try {
            byte[] document = s3service.downloaProfileAndDocumentsOfBikeProvider(userId, docType);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(document, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping("{bikeUserId}/image/delete/Licence")

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_BIKEPROVIDER_USER') ")
    public ResponseEntity<String> deleteLicence(@PathVariable("bikeUserId") UUID userId) {
        try {
            s3service.deleteProfileAndDocumentsOfBikeProvider(userId, "Rto_Licence");
            return ResponseEntity.ok("Document deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete document.");
        }
    }
    @DeleteMapping("{bikeUserId}/image/delete/Adhar")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_BIKEPROVIDER_USER') ")
    public ResponseEntity<String> deleteAdhar(@PathVariable("bikeUserId") UUID userId) {
        try {
            s3service.deleteProfileAndDocumentsOfBikeProvider(userId, "Aadhar");
            return ResponseEntity.ok("Document deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete document.");
        }
    }
    @DeleteMapping("{bikeUserId}/image/delete/Profile")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_BIKEPROVIDER_USER') ")

    public ResponseEntity<String> deleteProfile(@PathVariable("bikeUserId") UUID userId) {
        try {
            s3service.deleteProfileAndDocumentsOfBikeProvider(userId, "Profile");
            return ResponseEntity.ok("Document deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete document.");
        }
    }

    @PutMapping("/{bikeUserId}/update")

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_BIKEPROVIDER_USER') ")
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

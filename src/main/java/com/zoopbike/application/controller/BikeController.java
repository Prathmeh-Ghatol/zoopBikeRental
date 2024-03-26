package com.zoopbike.application.controller;

import com.zoopbike.application.dto.BikeDto;
import com.zoopbike.application.dto.BikeReturnDto;
import com.zoopbike.application.dto.GenricPage;
import com.zoopbike.application.service.impl.BikeSeImpl;
import com.zoopbike.application.service.impl.BookingService;
import com.zoopbike.application.service.impl.S3ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import static com.zoopbike.application.utils.zoopBikeRentalApplicationConstant.defaultApplicationPageSize;
import static com.zoopbike.application.utils.zoopBikeRentalApplicationConstant.defualtApplicationPageNO;

@RestController

@RequestMapping(value = "/bike/service")
public class BikeController {


    @Autowired
    BikeSeImpl bikeService;

    @Autowired
    S3ServiceImpl s3Service;
    @Autowired
    BookingService bookingService;

    @PostMapping(value = "/add/{bikeVenderEmail}")
    @PreAuthorize("hasRole('ROLE_BIKEPROVIDER_USER')")
    public ResponseEntity<BikeReturnDto> addBike(@RequestBody BikeDto bikeDto, @PathVariable("bikeVenderEmail") String bikeVenderEmail) throws InterruptedException {
        BikeReturnDto bike = this.bikeService.addBike(bikeDto, bikeVenderEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(bike);
    }

    @PutMapping(value = "/update/status/{uuid}")
    @PreAuthorize("hasRole('ROLE_BIKEPROVIDER_USER') and hasRole('ROLE_ADMIN')")
    public ResponseEntity<BikeReturnDto> updateStatus(@RequestBody BikeDto bikeDto, @PathVariable("uuid") UUID bikeID) {
        BikeReturnDto bikeReturnDto = this.bikeService.updateBike(bikeDto, bikeID);
        return ResponseEntity.status(HttpStatus.OK).body(bikeReturnDto);
    }

    @GetMapping(value = "/get/{uuid}")

    @PreAuthorize("hasRole('ROLE_BIKEPROVIDER_USER') and hasRole('ROLE_ADMIN') and hasRole('ROLE_APPLICATION_USER')")
    public ResponseEntity<BikeReturnDto> getBike(@PathVariable("uuid") UUID bikeId) {
        BikeReturnDto bike = this.bikeService.getBikeById(bikeId);
        return ResponseEntity.status(HttpStatus.OK).body(bike);
    }

    @DeleteMapping(value = "/delete/{uuid}")
    @PreAuthorize("hasRole('ROLE_BIKEPROVIDER_USER') and hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> deleteBike(@PathVariable("uuid") UUID bikeId) {
        Boolean bike = this.bikeService.deleteBike(bikeId);
        return ResponseEntity.status(HttpStatus.OK).body(bike);
    }

    @GetMapping(value = "/get/all/bikeprovider/{email}")
    @PreAuthorize("hasRole('ROLE_BIKEPROVIDER_USER') and hasRole('ROLE_ADMIN') and hasRole('ROLE_APPLICATION_USER')")
    public ResponseEntity<GenricPage<BikeReturnDto>> getAllBikes(
            @RequestParam(value = "pageNo", defaultValue = defualtApplicationPageNO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = defaultApplicationPageSize, required = false) int pageSize,
            @PathVariable("email") String bikeProviderEmail) {
        GenricPage<BikeReturnDto> bikes = this.bikeService.getAllBikeOfBikeVender(bikeProviderEmail, pageNo, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(bikes);
    }

    @PostMapping("/{bikeId}/upload/documents")
    @PreAuthorize("hasRole('ROLE_BIKEPROVIDER_USER')")

    public ResponseEntity<String> uploadDocument(
            @PathVariable UUID bikeId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") String documentType) {
        try {
            s3Service.bikeDocumentUpload(bikeId, file, documentType);
            return ResponseEntity.ok("Document uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload document.");
        }
    }

    @PostMapping("/{bikeId}/image/upload")
    @PreAuthorize("hasRole('ROLE_BIKEPROVIDER_USER')")
    public ResponseEntity<String> uploadImages(
            @PathVariable UUID bikeId,
            @RequestParam("files")  Set<MultipartFile>files) {
        try {
            s3Service.bikeImageUpload(bikeId, files);
            return ResponseEntity.ok("Images uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload images.");
        }
    }

        @GetMapping("/{bikeId}/images")
        @PreAuthorize("hasRole('ROLE_BIKEPROVIDER_USER') and hasRole('ROLE_ADMIN') and hasRole('ROLE_APPLICATION_USER')")
        public ResponseEntity<Set<byte[]>> downloadBikeImages(@PathVariable UUID bikeId) {
            try {
                Set<byte[]> imagesData = s3Service.downloadBikeImages(bikeId);
                return ResponseEntity.ok(imagesData);
            } catch (IOException e) {
                e.printStackTrace(); // Log the exception
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }




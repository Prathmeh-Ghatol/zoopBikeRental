package com.zoopbike.application.controller;

import com.zoopbike.application.service.impl.S3ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/img")
public class ImageUploader {
    @Value(value = "${bucket.user}")
    String bucketname;

@Autowired
private S3ServiceImpl service;
    @PostMapping(value = "/post/image")
    public ResponseEntity<Boolean>upload(@RequestParam(value = "imageUpload")MultipartFile file) throws IOException {
        UUID userId= UUID.fromString("5dc9e1f8-b47c-4147-9ae5-70740e5947e8");
        service.uploadImagesRelatedToBikeProvider(userId,file,"Aadhar");
        return ResponseEntity.ok(Boolean.TRUE);

    }
}

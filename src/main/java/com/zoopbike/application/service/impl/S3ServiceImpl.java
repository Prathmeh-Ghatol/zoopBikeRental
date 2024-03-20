package com.zoopbike.application.service.impl;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.zoopbike.application.config.S3BucketConfig;
import com.zoopbike.application.entity.ApplicationUser;
import com.zoopbike.application.entity.Bike;
import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.exception.ApplicationUserException;
import com.zoopbike.application.exception.BadBikeException;
import com.zoopbike.application.exception.BikeProviderPartnerException;
import com.zoopbike.application.repo.ApplicationUserRepo;
import com.zoopbike.application.repo.BikePartnerRepo;
import com.zoopbike.application.repo.BikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Service
public class S3ServiceImpl {

    private static final String USER_IMAGES_PATH = "user/";
    private static final String BIKEPROVIDER_IMAGES_PATH = "bike_provider/";
    private static final String BIKE_DOCUMENT_PATH = "bike_document/";

    private final static String BIKE_IMG = "bike_img/";
    private S3BucketConfig s3BucketConfig;
    @Value("${bucket.user}")
    private String bucketName;
    private ApplicationUserRepo applicationUserRepo;
    private BikeRepo bikeRepo;
    private BikePartnerRepo bikePartnerRepo;


    @Autowired
    S3ServiceImpl(S3BucketConfig s3BucketConfig, ApplicationUserRepo applicationUserRepo, BikeRepo bikeRepo,
                  BikePartnerRepo bikePartnerRepo) {
        this.s3BucketConfig = s3BucketConfig;
        this.applicationUserRepo = applicationUserRepo;
        this.bikeRepo = bikeRepo;
        this.bikePartnerRepo = bikePartnerRepo;
    }

    public void uploadImagesRelatedToUser(UUID userId, MultipartFile file, String imageTypeToUpload) throws IOException {
        ApplicationUser applicationUser = this.applicationUserRepo.findById(userId).orElseThrow(() -> new ApplicationUserException("User Not Found !!", userId.toString()));
        String property;
        final String path;
        switch (imageTypeToUpload) {
            case "Profile":
                path = USER_IMAGES_PATH + userId + "/profileImage/";
                property = "Profile";
                break;
            case "Rto_Licence":
                path = USER_IMAGES_PATH + userId + "/licence/";
                property = "Rto_Licence";
                break;
            case "Aadhar":
                path = USER_IMAGES_PATH + userId + "/adhar/";
                property = "Aadhar";
                break;
            default:
                throw new IllegalArgumentException("Invalid image type: " + imageTypeToUpload);
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String fullPath = path + fileName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fullPath, file.getInputStream(), null);
        s3BucketConfig.amazonS3().putObject(putObjectRequest);
        if (property == "Profile") {
            applicationUser.setProfileImage(fileName);

        } else if (property == "Rto_Licence") {
            applicationUser.setDrivingLicence(fileName);
        } else if (property == "Aadhar") {
            applicationUser.setAdhar(fileName);
        } else {
            throw new IllegalArgumentException("Invalid image type: " + imageTypeToUpload);
        }
        this.applicationUserRepo.save(applicationUser);

    }

    public void uploadImagesRelatedToBikeProvider(UUID userId, MultipartFile file, String imageTypeToUpload) throws IOException {
        BikeProviderPartner bikeProviderPartner = this.bikePartnerRepo.findById(userId).orElseThrow(() -> new ApplicationUserException("Bike Partner Not Found !!", userId.toString()));
        String path;
        String property;
        switch (imageTypeToUpload) {
            case "Profile":
                path = BIKEPROVIDER_IMAGES_PATH + userId + "/profileImage/";
                property = "Profile";
                break;
            case "Rto_Licence":
                path = BIKEPROVIDER_IMAGES_PATH + userId + "/licence/";
                property = "Rto_Licence";
                break;
            case "Aadhar":
                path = BIKEPROVIDER_IMAGES_PATH + userId + "/adhar/";
                property = "Aadhar";
                break;
            default:
                throw new IllegalArgumentException("Invalid image type: " + imageTypeToUpload);
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String fullPath = path + fileName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fullPath, file.getInputStream(), null);
        s3BucketConfig.amazonS3().putObject(putObjectRequest);
        if (property == "Profile") {
            bikeProviderPartner.setProfileImage(fileName);

        } else if (property == "Rto_Licence") {
            bikeProviderPartner.setDrivingLicence(fileName);
        } else if (property == "Aadhar") {
            bikeProviderPartner.setAdhar(fileName);
        } else {
            throw new IllegalArgumentException("Invalid image type: " + imageTypeToUpload);
        }
        this.bikePartnerRepo.save(bikeProviderPartner);
    }


    public void bikeImageUpload(UUID bikeId, Set<MultipartFile> files) throws IOException {
        Bike bike = this.bikeRepo.findById(bikeId).orElseThrow(() -> new BadBikeException("Bike is not present with bike id", bikeId.toString()));
        String path = BIKE_IMG + bikeId + "/";
        Set<String> images = new HashSet<>();
        for (MultipartFile file : files) {
            String fileName =file.getOriginalFilename();
            String fullPath = path + fileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fullPath, file.getInputStream(), null);
            s3BucketConfig.amazonS3().putObject(putObjectRequest);
            images.add(fileName);
        }
        bike.setBikeImages(images);
        this.bikeRepo.save(bike);
    }
    public Set<byte[]> downloadBikeImages(UUID bikeId) throws IOException {
        Bike bike = this.bikeRepo.findById(bikeId).orElseThrow(() -> new BadBikeException("Bike is not present ", bikeId.toString()));
        Set<String> imageNames = bike.getBikeImages();
        Set<byte[]> imagesData = new HashSet<>();

        for (String imageName : imageNames) {
            String path = BIKE_IMG + bikeId + "/" + imageName;
            S3Object s3Object = s3BucketConfig.amazonS3().getObject(bucketName, path);
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
            byte[] imageData = IOUtils.toByteArray(objectInputStream);
            imagesData.add(imageData);
            objectInputStream.close(); // Close the input stream after reading the image data
        }

        return imagesData;
    }
    public Set<byte[]> update (UUID bikeId) throws IOException {
        Bike bike = this.bikeRepo.findById(bikeId)
                .orElseThrow(() -> new BadBikeException("Bike is not present ", bikeId.toString()));
        Set<String> imageNames = bike.getBikeImages();
        Set<byte[]> imagesData = new HashSet<>();

        for (String imageName : imageNames) {
            String path = BIKE_IMG + bikeId + "/" + imageName;
            S3Object s3Object = null;
            S3ObjectInputStream objectInputStream = null;
            try {
                s3Object = s3BucketConfig.amazonS3().getObject(bucketName, path);
                objectInputStream = s3Object.getObjectContent();
                byte[] imageData = IOUtils.toByteArray(objectInputStream);
                imagesData.add(imageData);
            } catch (IOException e) {
                // Handle or log the exception
                e.printStackTrace();
            } finally {
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e) {
                        // Handle or log the exception
                        e.printStackTrace();
                    }
                }
            }
        }

        return imagesData;
    }


    public void bikeDocumentUpload(UUID bikeId, MultipartFile file, String documentType) throws IOException {
        Bike bike = this.bikeRepo.findById(bikeId).orElseThrow(() -> new BadBikeException("Bike is not present with bike id", bikeId.toString()));
        String path = BIKE_DOCUMENT_PATH + bikeId + "/";
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String fullPath = path + fileName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fullPath, file.getInputStream(), null);
        s3BucketConfig.amazonS3().putObject(putObjectRequest);
        // Update the appropriate field based on the document type
        switch (documentType) {
            case "RC":
                bike.setBIKE_RC(fileName);
                break;
            case "Insurance":
                bike.setBIKE_INSURANCE(fileName);
                break;
            case "PUC":
                bike.setBIKE_PUC(fileName);
                break;
            default:
                throw new IllegalArgumentException("Invalid document type: " + documentType);
        }

        this.bikeRepo.save(bike);
    }

    /***download**/
    public byte[] downloadProfileAndDocumentsOfApplicationUser(UUID applicationUserId, String DocumentType) throws IOException {
        ApplicationUser applicationUser = this.applicationUserRepo.findById(applicationUserId).orElseThrow(() ->
                new ApplicationUserException("Application User Not Found with ID", applicationUserId.toString()));

        S3Object s3Object = null;
        final String path;
        UUID userId;
        switch (DocumentType) {
            case "Profile":
                userId = applicationUser.getApplicationUserId();
                String fileName = applicationUser.getProfileImage();
                path = USER_IMAGES_PATH + userId + "/profileImage/" + fileName;

                s3Object = this.s3BucketConfig.amazonS3().getObject(bucketName, path);
                break;
            case "Rto_Licence":
                userId = applicationUser.getApplicationUserId();
                fileName = applicationUser.getDrivingLicence();
                path = USER_IMAGES_PATH + userId + "/licence/" + fileName;
                s3Object = this.s3BucketConfig.amazonS3().getObject(bucketName, path);
                break;
            case "Aadhar":
                userId = applicationUser.getApplicationUserId();
                fileName = applicationUser.getAdhar();
                path = USER_IMAGES_PATH + userId + "/adhar/" + fileName;
                System.out.println("path " +path);
                s3Object = this.s3BucketConfig.amazonS3().getObject(bucketName, path);
        }
        S3ObjectInputStream stream = s3Object.getObjectContent();
        byte[] document = IOUtils.toByteArray(stream);
        return document;

    }

    public void deleteProfileAndDocumentsOfApplicationUser(UUID applicationUserId, String documentType) throws IOException {
        ApplicationUser applicationUser = this.applicationUserRepo.findById(applicationUserId)
                .orElseThrow(() -> new ApplicationUserException("Application User Not Found with ID", applicationUserId.toString()));

        final String path;
        UUID userId;
        String property;

        switch (documentType) {
            case "Profile":
                userId = applicationUser.getApplicationUserId();
                String fileName = applicationUser.getProfileImage();
                path = USER_IMAGES_PATH + userId + "/profileImage/" + fileName;
                property = "Profile";
                break;
            case "Rto_Licence":
                userId = applicationUser.getApplicationUserId();
                fileName = applicationUser.getDrivingLicence();
                path = USER_IMAGES_PATH + userId + "/licence/" + fileName;
                property = "Rto_Licence";
                break;
            case "Aadhar":
                userId = applicationUser.getApplicationUserId();
                fileName = applicationUser.getProfileImage();
                path = USER_IMAGES_PATH + userId + "/adhar/" + fileName;
                property = "Aadhar";
                break;
            default:
                throw new IllegalArgumentException("Invalid document type: " + documentType);
        }

        // Delete the object from S3 bucket
        this.s3BucketConfig.amazonS3().deleteObject(bucketName, path);
        if (property == "Profile") {
            applicationUser.setProfileImage(null);

        } else if (property == "Rto_Licence") {
            applicationUser.setDrivingLicence(null);
        } else if (property.equals("Aadhar")) {
            applicationUser.setAdhar(null);
        } else {
            throw new IllegalArgumentException("Invalid image type: " + documentType);
        }
        applicationUserRepo.save(applicationUser);

    }

    public void updateProfileAndDocumentsOfApplicationUser(UUID applicationUserId, String documentType, MultipartFile file) throws IOException {
        ApplicationUser applicationUser = this.applicationUserRepo.findById(applicationUserId)
                .orElseThrow(() -> new ApplicationUserException("Application User Not Found with ID", applicationUserId.toString()));

        final String path;
        UUID userId;
        String property;
        String fileName;

        switch (documentType) {
            case "Profile":
                userId = applicationUser.getApplicationUserId();
                fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                path = USER_IMAGES_PATH + userId + "/profileImage/" + fileName;
                property = "Profile";
                break;
            case "Rto_Licence":
                userId = applicationUser.getApplicationUserId();
                fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                path = USER_IMAGES_PATH + userId + "/licence/" + fileName;
                property = "Rto_Licence";
                break;
            case "Aadhar":
                userId = applicationUser.getApplicationUserId();
                fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                path = USER_IMAGES_PATH + userId + "/adhar/" + fileName;
                property = "Aadhar";
                break;
            default:
                throw new IllegalArgumentException("Invalid document type: " + documentType);
        }

        // Upload the file to S3 bucket
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, file.getInputStream(), null);
        s3BucketConfig.amazonS3().putObject(putObjectRequest);

        // Update the corresponding property in ApplicationUser entity
        if (property.equals("Profile")) {
            applicationUser.setProfileImage(fileName);
        } else if (property.equals("Rto_Licence")) {
            applicationUser.setDrivingLicence(fileName);
        } else if (property.equals("Aadhar")) {
            applicationUser.setAdhar(fileName);
        } else {
            throw new IllegalArgumentException("Invalid document type: " + documentType);
        }
        // Save the updated ApplicationUser entity
        this.applicationUserRepo.save(applicationUser);
    }

    public byte[] downloaProfileAndDocumentsOfBikeProvider(UUID bikeProviderId, String DocumentType) throws IOException {
        BikeProviderPartner bikeProviderPartner = this.bikePartnerRepo.findById(bikeProviderId).orElseThrow(() ->
                new BikeProviderPartnerException(" User Not Found with ID", bikeProviderId.toString()));

        S3Object s3Object = null;
        final String path;
        UUID userId;
        switch (DocumentType) {
            case "Profile":
                userId = bikeProviderPartner.getBikeProviderPartnerId();
                String fileName = bikeProviderPartner.getProfileImage();
                path = BIKEPROVIDER_IMAGES_PATH + userId + "/profileImage/" + fileName;

                s3Object = this.s3BucketConfig.amazonS3().getObject(bucketName, path);
                break;
            case "Rto_Licence":
                userId = bikeProviderPartner.getBikeProviderPartnerId();
                fileName = bikeProviderPartner.getDrivingLicence();
                path = BIKEPROVIDER_IMAGES_PATH + userId + "/licence/" + fileName;
                s3Object = this.s3BucketConfig.amazonS3().getObject(bucketName, path);
                break;
            case "Aadhar":
                userId = bikeProviderPartner.getBikeProviderPartnerId();
                fileName = bikeProviderPartner.getAdhar();
                path = BIKEPROVIDER_IMAGES_PATH + userId + "/adhar/" + fileName;
                s3Object = this.s3BucketConfig.amazonS3().getObject(bucketName, path);
        }
        S3ObjectInputStream stream = s3Object.getObjectContent();
        byte[] document = IOUtils.toByteArray(stream);
        return document;

    }

    public void deleteProfileAndDocumentsOfBikeProvider(UUID bikeProviderId, String documentType) throws IOException {
        BikeProviderPartner bikeProviderPartner = this.bikePartnerRepo.findById(bikeProviderId).orElseThrow(() ->
                new BikeProviderPartnerException(" User Not Found with ID", bikeProviderId.toString()));

        final String path;
        UUID userId;
        String property;

        switch (documentType) {
            case "Profile":
                userId = bikeProviderPartner.getBikeProviderPartnerId();
                String fileName = bikeProviderPartner.getProfileImage();
                path = BIKEPROVIDER_IMAGES_PATH + userId + "/profileImage/" + fileName;
                property = "Profile";
                break;
            case "Rto_Licence":
                userId = bikeProviderPartner.getBikeProviderPartnerId();
                fileName = bikeProviderPartner.getDrivingLicence();
                path = BIKEPROVIDER_IMAGES_PATH + userId + "/licence/" + fileName;
                property = "Rto_Licence";
                break;
            case "Aadhar":
                userId = bikeProviderPartner.getBikeProviderPartnerId();
                fileName = bikeProviderPartner.getProfileImage();
                path = BIKEPROVIDER_IMAGES_PATH + userId + "/adhar/" + fileName;
                property = "Aadhar";
                break;
            default:
                throw new IllegalArgumentException("Invalid document type: " + documentType);
        }

        // Delete the object from S3 bucket
        this.s3BucketConfig.amazonS3().deleteObject(bucketName, path);
        if (property == "Profile") {
            bikeProviderPartner.setProfileImage(null);

        } else if (property == "Rto_Licence") {
            bikeProviderPartner.setDrivingLicence(null);
        } else if (property.equals("Aadhar")) {
            bikeProviderPartner.setDrivingLicence(null);
        } else {
            throw new IllegalArgumentException("Invalid image type: " + documentType);
        }
        bikePartnerRepo.save(bikeProviderPartner);

    }

    public void updateProfileAndDocumentsOfBikeProvider(UUID bikeProviderId, String documentType, MultipartFile file) throws IOException {
        BikeProviderPartner bikeProviderPartner = this.bikePartnerRepo.findById(bikeProviderId).orElseThrow(() ->
                new BikeProviderPartnerException(" User Not Found with ID", bikeProviderId.toString()));
        final String path;
        UUID userId;
        String property;
        String fileName;

        switch (documentType) {
            case "Profile":
                userId = bikeProviderPartner.getBikeProviderPartnerId();
                fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                path = BIKEPROVIDER_IMAGES_PATH + userId + "/profileImage/" + fileName;
                property = "Profile";
                break;
            case "Rto_Licence":
                userId = bikeProviderPartner.getBikeProviderPartnerId();
                fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                path = BIKEPROVIDER_IMAGES_PATH + userId + "/licence/" + fileName;
                property = "Rto_Licence";
                break;
            case "Aadhar":
                userId = bikeProviderPartner.getBikeProviderPartnerId();
                fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                path = BIKEPROVIDER_IMAGES_PATH + userId + "/adhar/" + fileName;
                property = "Aadhar";
                break;
            default:
                throw new IllegalArgumentException("Invalid document type: " + documentType);
        }

        // Upload the file to S3 bucket
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, file.getInputStream(), null);
        s3BucketConfig.amazonS3().putObject(putObjectRequest);

        // Update the corresponding property in ApplicationUser entity
        if (property.equals("Profile")) {
            bikeProviderPartner.setProfileImage(fileName);
        } else if (property.equals("Rto_Licence")) {
            bikeProviderPartner.setDrivingLicence(fileName);
        } else if (property.equals("Aadhar")) {
            bikeProviderPartner.setAdhar(fileName);
        } else {
            throw new IllegalArgumentException("Invalid document type: " + documentType);
        }
        // Save the updated ApplicationUser entity
        this.bikePartnerRepo.save(bikeProviderPartner);
    }


}



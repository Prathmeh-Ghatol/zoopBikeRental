package com.zoopbike.application.config;


import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3BucketConfig {


    @Value("${cloud.aws.credentials.secret-access-key}")
    private String SecrateaccessKey;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.region}")
    private String withRegion;

    public AmazonS3 amazonS3(){
        AWSCredentials awsCredentials=new BasicAWSCredentials(accessKey,SecrateaccessKey);
       return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(withRegion).build();

    }
}

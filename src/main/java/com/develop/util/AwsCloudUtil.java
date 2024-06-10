package com.develop.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AwsCloudUtil {

    private AWSCredentials awsCredentials(String accessKey, String secretKey) {

        return new BasicAWSCredentials(
                accessKey,
                secretKey
        );
    }

    private AmazonS3 awsS3ClientBuilder(String accessKey, String secretKey) {

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials(accessKey, secretKey)))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

    public void uploadFileToS3(String filename, byte[] bytes, String accessKey, String secretKey, String bucketName) {
        AmazonS3 s3Client = awsS3ClientBuilder(accessKey, secretKey);
        File file = new File(filename);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        s3Client.putObject(bucketName, filename, file);
    }

    public S3ObjectInputStream downloadFileFromS3(String filename, String accessKey, String secretKey, String bucketName) {
        AmazonS3 s3Client = awsS3ClientBuilder(accessKey, secretKey);
        S3Object s3Object = s3Client.getObject(bucketName, filename);
        return s3Object.getObjectContent();
    }
}

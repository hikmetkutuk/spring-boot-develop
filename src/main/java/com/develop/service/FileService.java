package com.develop.service;

import com.develop.model.File;
import com.develop.repository.FileRepository;
import com.develop.util.AwsCloudUtil;
import com.develop.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class FileService {

    @Value("${s3.bucket}")
    private String AWS_BUCKET;

    @Value("${s3.access-key}")
    private String AWS_ACCESS_KEY;

    @Value("${s3.secret-key}")
    private String AWS_SECRET_KEY;

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public File saveFile(File file) {
        return fileRepository.save(file);
    }

    public File getFile(String fileName) {
        return fileRepository.findFirstByFileName(fileName);
    }

    public byte[] downloadFileFromS3(String fileName) {
        AwsCloudUtil awsCloudUtil = new AwsCloudUtil();
        InputStream inputStream = awsCloudUtil.downloadFileFromS3(fileName, AWS_ACCESS_KEY, AWS_SECRET_KEY, AWS_BUCKET);

        String resourcesPath = "src/main/resources/static/img/";
        java.io.File targetFile = new java.io.File(resourcesPath + fileName);

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            byte[] fileContent = StreamUtils.copyToByteArray(inputStream);
            outputStream.write(fileContent);
            log.info("File downloaded: {}", fileName);
            return fileContent;
        } catch (IOException e) {
            log.error("Error downloading file from S3: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void uploadMultipartFile(MultipartFile data) {
        String fileName = data.getOriginalFilename();
        String fileType = data.getContentType();

        File existingFile = fileRepository.findFirstByFileName(fileName);
        if (existingFile != null && existingFile.getFileType().equals(fileType)) {
            log.info("File already exists: {}", fileName);
            return;
        }

        File file = new File();
        file.setFileName(fileName);
        file.setFileType(fileType);

        try {
            file.setFileByte(FileUtil.compressFile(data.getBytes()));
        } catch (IOException e) {
            log.error("Error compressing file: {}", e.getMessage());
            e.printStackTrace();
        }

        log.info("File uploaded: {}", fileName);
        File newFile = saveFile(file);
        fileRepository.save(newFile);
    }

    public String uploadFileToS3(MultipartFile data) {
        try {
            uploadMultipartFile(data);

            AwsCloudUtil awsCloudUtil = new AwsCloudUtil();
            awsCloudUtil.uploadFileToS3(data.getOriginalFilename(), data.getBytes(), AWS_ACCESS_KEY, AWS_SECRET_KEY, AWS_BUCKET);
            return String.format("File %s uploaded successfully", data.getOriginalFilename());
        } catch (IOException e) {
            log.error("Error uploading file to S3: {}", e.getMessage());
            e.printStackTrace();
        }
        return String.format("File %s not uploaded", data.getOriginalFilename());
    }
}

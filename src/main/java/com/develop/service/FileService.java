package com.develop.service;

import com.develop.model.File;
import com.develop.repository.FileRepository;
import com.develop.util.AwsCloudUtil;
import com.develop.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        return fileRepository.findByFileName(fileName);
    }

    public void deleteFile(String fileName) throws Exception {
        File file = getFile(fileName);
        if (file == null) {
            throw new Exception(String.format("File %s not found", fileName));
        }
        fileRepository.delete(file);
    }

    public byte[] downloadFile(String fileName) {
        return FileUtil.decompressFile(getFile(fileName).getFileByte());
    }

    public byte[] downloadFileFromS3(String fileName) {
        try {
            AwsCloudUtil awsCloudUtil = new AwsCloudUtil();
            return awsCloudUtil.downloadFileFromS3(fileName, AWS_ACCESS_KEY, AWS_SECRET_KEY, AWS_BUCKET).readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String uploadMultipartFile(MultipartFile data) {
        File file = new File();
        file.setFileName(data.getOriginalFilename());
        file.setFileType(data.getContentType());

        try {
            file.setFileByte(FileUtil.compressFile(data.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        File newFile = saveFile(file);

        if (newFile != null) {
            return String.format("File %s uploaded successfully", newFile.getFileName());
        }

        return String.format("File %s not uploaded", file.getFileName());
    }

    public String uploadFileToS3(MultipartFile data) {
        try {
            uploadMultipartFile(data);

            AwsCloudUtil awsCloudUtil = new AwsCloudUtil();
            awsCloudUtil.uploadFileToS3(data.getOriginalFilename(), data.getBytes(), AWS_ACCESS_KEY, AWS_SECRET_KEY, AWS_BUCKET);
            return String.format("File %s uploaded successfully", data.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.format("File %s not uploaded", data.getOriginalFilename());
    }
}

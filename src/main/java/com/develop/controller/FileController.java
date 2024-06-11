package com.develop.controller;

import com.develop.dto.HttpResponse;
import com.develop.model.File;
import com.develop.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Uploads a file to the server and returns a response entity.
     *
     * @param  file	The file to be uploaded
     * @return     	Response entity containing the upload status
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String response = fileService.uploadFileToS3(file);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of("response", response))
                        .message("File uploaded successfully")
                        .path("/api/v1/file/upload")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    /**
     * Downloads a file based on the provided fileName.
     *
     * @param  fileName     the name of the file to be downloaded
     * @return              response entity with the downloaded file
     */
    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileName") String fileName) {
        File fileDetails = fileService.getFile(fileName);
        byte[] file = fileService.downloadFileFromS3(fileName);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(fileDetails.getFileType()))
                .body(file);
    }
}

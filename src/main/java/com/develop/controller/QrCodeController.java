package com.develop.controller;

import com.develop.service.QrCodeService;
import com.google.zxing.WriterException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Random;

@Controller
@RequestMapping("/api/v1/qrcode")
public class QrCodeController {
    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    /**
     * Generate a QR Code image based on the input text.
     *
     * @param text the text to be encoded in the QR Code
     * @return a ResponseEntity containing the QR Code image as a byte array
     */
    @GetMapping(value = "/generate/{text}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQRCodeWithText(@PathVariable("text") String text) {
        try {
            // Generate QR Code as byte array
            byte[] qrCodeImage = qrCodeService.getQRCodeImage(text, 250, 250);

            // Return the image directly
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"").body(qrCodeImage);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Generate a QR code image and return it as a ResponseEntity.
     *
     * @return ResponseEntity<byte [ ]> containing the QR code image
     */
    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQRCode() {
        try {
            String generatedString = generateRandomString();

            // Generate QR Code as byte array
            byte[] qrCodeImage = qrCodeService.getQRCodeImage(generatedString, 250, 250);

            // Return the image directly
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"").body(qrCodeImage);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    private String generateRandomString() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}

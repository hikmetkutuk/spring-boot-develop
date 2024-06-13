package com.develop.controller;

import com.develop.service.QrCodeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Random;

@Controller
@RequestMapping("/api/v1/qrcode")
public class QrCodeController {
    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    /**
     * Generates a QR code image with the provided text.
     *
     * @param text the text to be encoded in the QR code
     * @return the ResponseEntity containing the QR code image
     */
    @GetMapping(value = "/generate/{text}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQRCodeWithText(@PathVariable("text") String text) {
        byte[] qrCodeImage = qrCodeService.getQRCodeImage(text, 250, 250);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"").body(qrCodeImage);
    }

    /**
     * Retrieves a QR code as a byte array by generating a random string,
     * creating a QR code image, and returning it within a ResponseEntity.
     *
     * @return ResponseEntity containing the QR code image
     */
    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQRCode() {
        String generatedString = generateRandomString();
        byte[] qrCodeImage = qrCodeService.getQRCodeImage(generatedString, 250, 250);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"").body(qrCodeImage);
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

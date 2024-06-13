package com.develop.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class QrCodeService {
    public byte[] getQRCodeImage(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageConfig matrixConfig = new MatrixToImageConfig(0xFF000002, 0xFFFFC041);

            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, matrixConfig);
            log.info("QR code generated successfully");
            return pngOutputStream.toByteArray();
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            log.error("QR code generation failed");
            return null;
        }
    }
}
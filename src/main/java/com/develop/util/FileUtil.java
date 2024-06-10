package com.develop.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class FileUtil {
    public static byte[] compressFile(byte[] file) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(file);
        deflater.finish();

        ByteArrayOutputStream stream = new ByteArrayOutputStream(file.length);
        byte[] tmp = new byte[4 * 1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            stream.write(tmp, 0, size);
        }
        try {
            stream.close();
        } catch (Exception ignored) {
        }
        return stream.toByteArray();
    }

    public static byte[] decompressFile(byte[] file) {
        Inflater inflater = new Inflater();
        inflater.setInput(file);

        ByteArrayOutputStream stream = new ByteArrayOutputStream(file.length);
        byte[] tmp = new byte[4 * 1024];

        try {
            while (!inflater.finished()) {
                int size = inflater.inflate(tmp);
                stream.write(tmp, 0, size);
            }
            stream.close();
        } catch (Exception ignored) {
        }
        return stream.toByteArray();
    }
}

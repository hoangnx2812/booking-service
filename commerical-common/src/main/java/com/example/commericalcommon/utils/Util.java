package com.example.commericalcommon.utils;

import com.example.commericalcommon.exception.ErrorCode;
import com.example.commericalcommon.exception.GlobalException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import static com.example.commericalcommon.enums.MimeType.getExtensionByMime;

@UtilityClass
@Slf4j
public class Util {
    private static final SecureRandom SR = new SecureRandom();
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("ddMMyyHHmmssSSS");

    public static String generateSalt(Integer numBytes) {
        if (numBytes == null || numBytes <= 0) {
            numBytes = 32;
        }
        byte[] b = new byte[numBytes];
        SR.nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }

    public static String generateNo(String prefix) {
        String timePart = LocalDateTime.now().format(TIME_FORMAT);
        String randPart = generateSalt(4).toUpperCase();
        return prefix + timePart + randPart;
    }

    public static String generateObjectName(String name, String type) {
        String timePart = LocalDateTime.now().format(TIME_FORMAT);
        String extension = getExtensionByMime(type);
        return name + "_" + timePart + extension;
    }

    public static String encryptSHA256(String input) {
        StringBuilder hex;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            log.error(getStackTrace(e));
            throw new GlobalException(ErrorCode.UNCATEGORIZED);
        }
        return hex.toString();
    }

    public static String getStackTrace(Exception e) {
        StringWriter sWriter = new StringWriter();
        PrintWriter pWriter = new PrintWriter(sWriter);
        e.printStackTrace(pWriter);
        return sWriter.toString();
    }

    public static LocalDateTime isNotNull(Timestamp obj) {
        return obj == null ? null : obj.toLocalDateTime();
    }

    public static String shaBase64(String base64Data, String algorithm) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            log.error("sha1FromBase64Raw error: {}", getStackTrace(e));
            throw new GlobalException(ErrorCode.UNCATEGORIZED);
        }
        byte[] digest = md.digest(base64Data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public static long getAttachmentSizeBase64(String base64) {
        if (base64.contains(",")) {
            base64 = base64.substring(base64.indexOf(",") + 1);
        }

        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        return decodedBytes.length;
    }

    public static String formatFileSize(long size) {
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int unitIndex = (int) (Math.log10(size) / Math.log10(1024));
        double formattedSize = size / Math.pow(1024, unitIndex);
        return String.format("%.2f %s", formattedSize, units[unitIndex]);
    }

    public static void main(String[] args) {
        log.info(encryptSHA256("6f2cb9dd8f4b65e24e1c3f3fa5bc57982349237f11abceacd45bbcb74d621c25"));
    }

}

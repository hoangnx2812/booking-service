package com.example.postservice.service.impl;

import com.example.postservice.service.MinioService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Base64;

import static com.example.commericalcommon.utils.Util.getStackTrace;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MinioServiceImpl implements MinioService {
    MinioClient minioClient;

    @NonFinal
    @Value("${minio.url}")
    String minioUrl;

    @NonFinal
    @Value("${minio.attachments-bucket}")
    String attachmentsBucket;

    @Override
    public void uploadBase64Image(String base64, String objectName, String contentType) {
        if (base64.contains(",")) {
            base64 = base64.split(",")[1];
        }

        byte[] data = Base64.getDecoder().decode(base64);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(attachmentsBucket)
                            .object(objectName)
                            .stream(bais, data.length, -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception e) {
            log.error(getStackTrace(e));
        }
    }

    @Override
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                    io.minio.RemoveObjectArgs.builder()
                            .bucket(attachmentsBucket)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error(getStackTrace(e));
        }
    }

    @Override
    public String getPresignedUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(attachmentsBucket)
                            .object(objectName)
                            .expiry(1, java.util.concurrent.TimeUnit.DAYS)
//                            .extraQueryParams(Map.of("response-content-disposition", "attachment; filename=\"" + objectName + "\""))
                            .build()
            );
        } catch (Exception e) {
            log.error(getStackTrace(e));
            return null;
        }
    }

    @Override
    public String createObjectName(String fileName, String folderName) {
        LocalDate today = LocalDate.now();
        return folderName + "/"
                + today.getYear() + "/"
                + today.getMonthValue() + "/"
                + today.getDayOfMonth() + "/"
                + fileName;
    }
}

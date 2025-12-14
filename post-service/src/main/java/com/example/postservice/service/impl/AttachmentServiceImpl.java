package com.example.postservice.service.impl;

import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.dto.object.AttachmentDTO;
import com.example.commericalcommon.dto.object.AttachmentMapDTO;
import com.example.commericalcommon.dto.request.AttachmentRequest;
import com.example.commericalcommon.dto.request.GetUserInfoRequest;
import com.example.commericalcommon.dto.response.user.UserInfoResponse;
import com.example.commericalcommon.enums.AttachmentFolder;
import com.example.commericalcommon.service.RedisService;
import com.example.commericalcommon.utils.Constant;
import com.example.commericalcommon.utils.RedisConstant;
import com.example.postservice.repository.httpclient.AuthenticationClient;
import com.example.postservice.repository.jdbc.AttachmentRepositoryJdbc;
import com.example.postservice.service.AttachmentService;
import com.example.postservice.service.MinioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.commericalcommon.utils.Constant.PrefixNo.ATTACHMENT_NO;
import static com.example.commericalcommon.utils.Constant.SUCCESS_CODE;
import static com.example.commericalcommon.utils.Util.*;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AttachmentServiceImpl implements AttachmentService {
    AttachmentRepositoryJdbc attachmentRepositoryJdbc;
    MinioService minioService;
    RedisService redisService;
    ObjectMapper objectMapper;
    AuthenticationClient authenticationClient;

    @Override
    @Transactional
    public void addAttachment(AttachmentRequest request, AttachmentFolder folderName,
                              String objectType, Long objectId, Integer orderNo) {
        String attData = request.getData();
        String fileName = request.getName();
        String fileType = request.getType();
        String shaSum = shaBase64(attData, Constant.Attachment.ALGORITHM_HASH);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BaseResponse<UserInfoResponse> userInfo =
                authenticationClient.getUserByConditions(GetUserInfoRequest.builder()
                        .userName(userName)
                        .build());
        log.info("Response from authentication-service: {}", userInfo);
        if (!SUCCESS_CODE.equals(userInfo.getResultCode())) {
            return;
        }

        // Check in Redis cache
        AttachmentDTO attCache = redisService.hget(shaSum, RedisConstant.Htable.ATTACHMENT_INFO, AttachmentDTO.class);

        if (attCache == null) {
            // If not found in cache, check in DB
            AttachmentDTO attachmentDTO = attachmentRepositoryJdbc.checkSumExists(shaSum);

            // If found in DB, cache it
            if (attachmentDTO != null) {
                try {
                    redisService.hset(shaSum,
                            RedisConstant.Htable.ATTACHMENT_INFO,
                            objectMapper.writeValueAsString(attachmentDTO),
                            RedisConstant.Htable.TTL_DEFAULT);
                } catch (JsonProcessingException e) {
                    log.error("Error serializing attachmentDTO for checksum {}", getStackTrace(e));
                }
                attCache = attachmentDTO;
            } else {
                // If not found in DB, insert new attachment

                String size = formatFileSize(getAttachmentSizeBase64(attData));
                String attachmentName = generateObjectName(fileName, fileType);
                String objectName = minioService.createObjectName(attachmentName, folderName.getFolderName());


                Long attachmentId = attachmentRepositoryJdbc.insertAttachment(
                        AttachmentDTO.builder()
                                .fileName(request.getName())
                                .mimeType(request.getType())
                                .thumbnail(objectName)
                                .filePathSm(objectName)
                                .filePathLg(objectName)
                                .filePathOriginal(objectName)
                                .description(request.getDescription())
                                .checksum(shaSum)
                                .createdBy(userInfo.getData().getId())
                                .size(size)
                                .folder(folderName.getFolderName())
                                .build());

                // Add to minio
                minioService.uploadBase64Image(attData, objectName, fileType);
                attCache = AttachmentDTO.builder()
                        .id(attachmentId)
                        .fileName(request.getName())
                        .build();
            }
        }

        // Insert attachment map
        attachmentRepositoryJdbc.insertAttachmentMap(AttachmentMapDTO.builder()
                .code(generateNo(ATTACHMENT_NO))
                .displayName(attCache.getFileName())
                .objectId(objectId)
                .objectType(objectType)
                .description(request.getDescription())
                .createdBy(userInfo.getData().getId())
                .orderNo(orderNo)
                .attachmentId(attCache.getId())
                .build());
    }
}

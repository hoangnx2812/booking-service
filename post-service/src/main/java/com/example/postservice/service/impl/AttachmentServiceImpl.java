package com.example.postservice.service.impl;

import com.example.commericalcommon.dto.object.AttachmentDTO;
import com.example.commericalcommon.dto.object.AttachmentMapDTO;
import com.example.commericalcommon.dto.request.AttachmentRequest;
import com.example.commericalcommon.dto.response.user.UserInfoResponse;
import com.example.commericalcommon.enums.AttachmentFolder;
import com.example.commericalcommon.enums.ObjectType;
import com.example.commericalcommon.utils.Constant;
import com.example.postservice.repository.jdbc.AttachmentRepositoryJdbc;
import com.example.postservice.service.AttachmentService;
import com.example.postservice.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.commericalcommon.utils.Constant.PrefixNo.ATTACHMENT_NO;
import static com.example.commericalcommon.utils.Util.*;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AttachmentServiceImpl implements AttachmentService {
    AttachmentRepositoryJdbc attachmentRepositoryJdbc;
    MinioService minioService;

    @Override
    @Transactional
    public void insertAttachment(List<AttachmentRequest> request, AttachmentFolder folderName,
                              ObjectType objectType, Long objectId, UserInfoResponse userInfo) {

        // List to Map for easy access
        Map<String, AttachmentRequest> attachmentMap = request.stream()
                .collect(Collectors.toMap(att -> shaBase64(att.getData(),
                        Constant.ALGORITHM_SHA1), Function.identity()));

        // Check exist attachments
        List<AttachmentDTO> attExists = attachmentRepositoryJdbc.checkSumExists(attachmentMap.keySet().stream().toList());

        // Filter not exist attachments
        List<String> attNotExists = attachmentMap.keySet().stream()
                .filter(sha -> attExists.stream().noneMatch(att -> att.getChecksum().equals(sha)))
                .toList();


        if (!CollectionUtils.isEmpty(attExists)) {
            attExists.forEach(att -> {
                AttachmentRequest attReq = attachmentMap.get(att.getChecksum());
                String name = attReq.getName();
                String description = attReq.getDescription();
                AtomicInteger orderNo = new AtomicInteger(0);

                attachmentRepositoryJdbc.insertAttachmentMap(AttachmentMapDTO.builder()
                        .code(generateNo(ATTACHMENT_NO))
                        .displayName(name)
                        .objectId(objectId)
                        .objectType(objectType.getType())
                        .description(description)
                        .createdBy(userInfo.getId())
                        .orderNo(orderNo.getAndIncrement())
                        .attachmentId(att.getId())
                        .build());
            });
        }

        if (!CollectionUtils.isEmpty(attNotExists)) {
            attNotExists.forEach(shaCheckSum -> {
                AttachmentRequest attReq = attachmentMap.get(shaCheckSum);
                String name = attReq.getName();
                String fileType = attReq.getType();
                String attData = attReq.getData();
                AtomicInteger orderNo = new AtomicInteger(0);

                String size = formatFileSize(getAttachmentSizeBase64(attData));
                String attachmentName = generateObjectName(name, fileType);
                String objectName = minioService.createObjectName(attachmentName, folderName.getFolderName());
                String description = attReq.getDescription();

                Long attachmentId = attachmentRepositoryJdbc.insertAttachment(
                        AttachmentDTO.builder()
                                .fileName(name)
                                .mimeType(fileType)
                                .thumbnail(objectName)
                                .filePathSm(objectName)
                                .filePathLg(objectName)
                                .filePathOriginal(objectName)
                                .description(description)
                                .checksum(shaCheckSum)
                                .createdBy(userInfo.getId())
                                .size(size)
                                .folder(folderName.getFolderName())
                                .build());

                // Add to minio
                minioService.uploadBase64Image(attData, objectName, fileType);

                attachmentRepositoryJdbc.insertAttachmentMap(AttachmentMapDTO.builder()
                        .code(generateNo(ATTACHMENT_NO))
                        .displayName(name)
                        .objectId(objectId)
                        .objectType(objectType.getType())
                        .description(description)
                        .createdBy(userInfo.getId())
                        .orderNo(orderNo.getAndIncrement())
                        .attachmentId(attachmentId)
                        .build());
            });
        }
    }

}

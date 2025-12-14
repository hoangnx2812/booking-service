package com.example.postservice.service;

import com.example.commericalcommon.dto.request.AttachmentRequest;
import com.example.commericalcommon.enums.AttachmentFolder;

public interface AttachmentService {
    void addAttachment(AttachmentRequest request, AttachmentFolder folderName, String objectType, Long objectId, Integer orderNo);
}

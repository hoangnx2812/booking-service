package com.example.postservice.service;

import com.example.commericalcommon.dto.request.AttachmentRequest;
import com.example.commericalcommon.dto.response.user.UserInfoResponse;
import com.example.commericalcommon.enums.AttachmentFolder;
import com.example.commericalcommon.enums.ObjectType;

import java.util.List;

public interface AttachmentService {
    void insertAttachment(List<AttachmentRequest> request, AttachmentFolder folderName, ObjectType objectType,
                       Long objectId, UserInfoResponse userInfo);
}

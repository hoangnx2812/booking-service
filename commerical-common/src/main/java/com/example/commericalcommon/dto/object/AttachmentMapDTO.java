package com.example.commericalcommon.dto.object;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class AttachmentMapDTO {
    Long id;
    String code;
    String displayName;
    String objectType;
    Long objectId;
    String clientUploadCode;
    String description;
    Long createdBy;
    String status;
    Integer orderNo;
    Long attachmentId;
}

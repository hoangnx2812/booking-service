package com.example.postservice.dto.request;

import com.example.commericalcommon.dto.request.AttachmentRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class InsertPostRequest {
    @NotBlank(message = "{CONTENT_MUST_NOT_BE_EMPTY}")
    String content;

    @Valid
    @NotEmpty(message = "{ATTACHMENTS_MUST_NOT_BE_EMPTY}")
    List<AttachmentRequest> attachments;

    List<String> hashtags;

    List<Long> serviceIds;
}

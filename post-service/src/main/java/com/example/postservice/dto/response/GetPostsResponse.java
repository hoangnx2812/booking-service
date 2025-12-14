package com.example.postservice.dto.response;

import com.example.commericalcommon.dto.response.AttachmentResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class GetPostsResponse {
    Long postId;
    Long userInfoId;
    String userAvatar;
    String userFullName;
    String postTitle;
    List<String> serviceName;
    String createdAt;
    String totalComments;
    String totalLikes;
    List<AttachmentResponse> attachments;

}

package com.example.commericalcommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum AttachmentFolder {
    USER_AVATAR("user_avatar/"),
    POST_IMAGE("post_image/"),
    DOCUMENT("document/"),
    OTHER("other/");

    String folderName;
}

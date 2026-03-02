package com.example.commericalcommon.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GetUserInfoRequest {
    Long userInfoId;
    String userName;
}

package com.example.commericalcommon.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    Long id;
    String userNo;
    String fullName;
    String email;
    String phoneNumber;
    Integer areaId;
    String addressDetail;
    String avatar;
    String gender;
}

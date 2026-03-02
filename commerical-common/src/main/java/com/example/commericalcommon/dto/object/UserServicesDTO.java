package com.example.commericalcommon.dto.object;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserServicesDTO {
    Long userServiceId;
    Long userServiceMapId;
    String name;
    Double price;
    Long userId;
    Long storeId;
    String time;
    String objectType;
    Long objectId;
}

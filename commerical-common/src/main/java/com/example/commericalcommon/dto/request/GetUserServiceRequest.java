package com.example.commericalcommon.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class GetUserServiceRequest {
    String title;
    Double priceFrom;
    Double priceTo;
    String duration;
    String objectId;
    String objectType;
}

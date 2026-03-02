package com.example.commericalcommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum ObjectType {
    POST("POST"),
    PROMOTION("PROMOTION"),
    HASHTAG("HASHTAG"),
    SERVICE("SERVICE");
    String type;
}

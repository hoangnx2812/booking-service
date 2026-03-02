package com.example.commericalcommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum Status {
    ACTIVE("A"),
    INACTIVE("I"),
    DELETED("D"),
    BLOCKED("B");

    String status;
}

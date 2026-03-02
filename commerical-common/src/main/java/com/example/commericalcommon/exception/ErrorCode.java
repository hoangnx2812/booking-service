package com.example.commericalcommon.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED("01", "UNCATEGORIZED", HttpStatus.INTERNAL_SERVER_ERROR),
    METHOD_NOT_ALLOWED("02", "METHOD_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED),
    UNAUTHORIZED("03", "UNAUTHORIZED", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED("04", "UNAUTHENTICATED", HttpStatus.UNAUTHORIZED),
    INVALID_INPUT("05", "INVALID_INPUT", HttpStatus.BAD_REQUEST),
    INVALID_ENDPOINT("06", "INVALID_ENDPOINT", HttpStatus.NOT_FOUND),
    RESOURCE_NOT_FOUND("07", "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND),
    USER_NOT_EXISTED("08", "USER_NOT_EXISTED", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXISTED("09", "ROLE_NOT_EXISTED", HttpStatus.NOT_FOUND),
    SESSION_EXPIRED("10", "SESSION_EXPIRED", HttpStatus.UNAUTHORIZED),
    POST_NOT_FOUND("11", "POST_NOT_FOUND", HttpStatus.NOT_FOUND)
    ;


    ErrorCode(String code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final String code;
    private final HttpStatusCode statusCode;
    private final String message;
}

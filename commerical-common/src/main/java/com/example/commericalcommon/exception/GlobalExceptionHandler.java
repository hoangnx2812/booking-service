package com.example.commericalcommon.exception;


import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.utils.MessageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {
    MessageUtil messageUtil;

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<BaseResponse<Object>> handlingRuntimeException(RuntimeException exception) {
        log.error("Exception: ", exception);
        return ResponseEntity
                .badRequest()
                .body(setBaseResponse(ErrorCode.UNCATEGORIZED.getCode(),
                        ErrorCode.UNCATEGORIZED.getMessage()));
    }

    @ExceptionHandler(value = GlobalException.class)
    ResponseEntity<BaseResponse<Object>> handlingAppException(GlobalException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(setBaseResponse(errorCode.getCode(), errorCode.getMessage()));
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<BaseResponse<Object>> handlingValidation(MethodArgumentNotValidException exception) {
        return ResponseEntity
                .badRequest()
                .body(setBaseResponse(ErrorCode.INVALID_INPUT.getCode(),
                        Objects.requireNonNull(exception.getFieldError()).getDefaultMessage()));
    }

//    @ExceptionHandler(value = NoResourceFoundException.class)
//    ResponseEntity<BaseResponse<Object>> handleNoResourceFoundException(NoResourceFoundException exception) {
//        return ResponseEntity
//                .badRequest()
//                .body(setBaseResponse(ErrorCode.RESOURCE_NOT_FOUND.getCode(),
//                        ErrorCode.RESOURCE_NOT_FOUND.getMessage()));
//    }
//
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public ResponseEntity<BaseResponse<Object>> handleHttpRequestMethodNotSupportedException() {
//        return ResponseEntity
//                .badRequest()
//                .body(setBaseResponse(ErrorCode.METHOD_NOT_ALLOWED.getCode(),
//                        ErrorCode.METHOD_NOT_ALLOWED.getMessage()));
//    }

    private BaseResponse<Object> setBaseResponse(String errorCode, String message) {
        return BaseResponse.builder()
                .resultDesc((messageUtil.getMessage(message)))
                .resultCode(errorCode)
                .build();
    }


}

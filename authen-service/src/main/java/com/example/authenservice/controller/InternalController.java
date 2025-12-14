package com.example.authenservice.controller;

import com.example.authenservice.service.UserService;
import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.dto.request.GetUserInfoRequest;
import com.example.commericalcommon.utils.MessageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InternalController {
    UserService userService;
    MessageUtil messageUtil;

    @GetMapping("/users/by-conditions")
    public BaseResponse<Object> getUserByConditions(@RequestBody GetUserInfoRequest request) {
        long startTime = System.currentTimeMillis();
        return BaseResponse.builder()
                .resultDesc(messageUtil.getMessage("FETCH_SUCCESS"))
                .data(userService.getUserByConditions(request))
                .cost(System.currentTimeMillis() - startTime)
                .build();
    }


}

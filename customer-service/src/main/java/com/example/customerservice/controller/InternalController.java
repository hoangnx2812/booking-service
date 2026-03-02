package com.example.customerservice.controller;

import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.dto.request.GetUserServiceRequest;
import com.example.commericalcommon.utils.MessageUtil;
import com.example.customerservice.service.UserServiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal")
public class InternalController {
    MessageUtil messageUtil;
    UserServiceService userServiceService;

    @PostMapping("/get-user-services")
    public BaseResponse<Object> getUserServicesByConditions(@RequestBody GetUserServiceRequest request) {
        long startTime = System.currentTimeMillis();
        return BaseResponse.builder()
                .resultDesc(messageUtil.getMessage("FETCH_SUCCESS"))
                .data(userServiceService.getUserServicesByConditions(request))
                .cost(System.currentTimeMillis() - startTime)
                .build();
    }
}

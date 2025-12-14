package com.example.postservice.repository.httpclient;

import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.dto.request.GetUserInfoRequest;
import com.example.commericalcommon.dto.response.user.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authentication-service", url = "${app.services.authentication.url}")
public interface AuthenticationClient {
    @GetMapping(value = "/internal/users/by-conditions",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    BaseResponse<UserInfoResponse> getUserByConditions(@RequestBody GetUserInfoRequest request);
}

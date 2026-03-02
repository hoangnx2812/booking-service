package com.example.postservice.repository.httpclient;

import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.dto.object.UserServicesDTO;
import com.example.commericalcommon.dto.request.GetUserServiceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "customer-service", url = "${app.services.customer.url}")
public interface CustomerClient {
    @PostMapping(value = "/internal/get-user-services",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    BaseResponse<List<UserServicesDTO>> getUserByConditions(@RequestBody GetUserServiceRequest request);
}

package com.example.customerservice.service;

import com.example.commericalcommon.dto.request.GetUserServiceRequest;

public interface UserServiceService {
    Object getUserServicesByConditions(GetUserServiceRequest request);
}

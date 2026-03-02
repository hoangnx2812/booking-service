package com.example.customerservice.service.impl;

import com.example.commericalcommon.dto.request.GetUserServiceRequest;
import com.example.customerservice.repository.jdbc.UserServiceJdbcRepository;
import com.example.customerservice.service.UserServiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceServiceImpl implements UserServiceService {
    UserServiceJdbcRepository userServiceJdbcRepository;

    @Override
    public Object getUserServicesByConditions(GetUserServiceRequest request) {
        return userServiceJdbcRepository.getServicesByConditions(request);
    }
}

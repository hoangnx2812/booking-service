package com.example.authenservice.service;


import com.example.commericalcommon.dto.request.GetUserInfoRequest;

public interface UserService {

    Object getUserInfo();

    Object getUserByConditions(GetUserInfoRequest request);
}

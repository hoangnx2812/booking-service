package com.example.postservice.service.impl;

import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.dto.request.GetUserInfoRequest;
import com.example.commericalcommon.dto.response.HashtagResponse;
import com.example.commericalcommon.dto.response.user.UserInfoResponse;
import com.example.commericalcommon.service.RedisService;
import com.example.postservice.repository.httpclient.AuthenticationClient;
import com.example.postservice.repository.jdbc.HashtagRepositoryJdbc;
import com.example.postservice.service.HashtagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.commericalcommon.utils.Constant.SUCCESS_CODE;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class HashtagServiceImpl implements HashtagService {
    HashtagRepositoryJdbc hashtagRepositoryJdbc;
    RedisService redisService;
    ObjectMapper objectMapper;
    AuthenticationClient authenticationClient;

    @Override
    public List<HashtagResponse> getHashtagsByCondition(Object request) {
        return List.of();
    }

    @Override
    public void addHashtags(String hashtag, String objectType, Long objectId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BaseResponse<UserInfoResponse> userInfo =
                authenticationClient.getUserByConditions(GetUserInfoRequest.builder()
                        .userName(userName)
                        .build());
        log.info("Response from authentication-service: {}", userInfo);
        if (!SUCCESS_CODE.equals(userInfo.getResultCode())) {
            return;
        }
    }
}

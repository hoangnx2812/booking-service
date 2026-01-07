package com.example.authenservice.service.impl;


import com.example.authenservice.entity.UserAuth;
import com.example.authenservice.entity.UserInfo;
import com.example.authenservice.repository.UserAuthRepository;
import com.example.authenservice.repository.UserInfoRepository;
import com.example.authenservice.service.UserService;
import com.example.commericalcommon.dto.request.GetUserInfoRequest;
import com.example.commericalcommon.dto.response.user.UserInfoResponse;
import com.example.commericalcommon.exception.ErrorCode;
import com.example.commericalcommon.exception.GlobalException;
import com.example.commericalcommon.service.RedisService;
import com.example.commericalcommon.utils.RedisConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserInfoRepository userInfoRepository;
    UserAuthRepository userAuthRepository;
    RedisService redisService;
    ObjectMapper objectMapper;

    @Override
    public Object getUserInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        UserInfo userInfo = userInfoRepository.findByUserAuth_UserName(name)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_EXISTED));

        return UserInfoResponse.builder()
                .userNo(userInfo.getUserNo())
                .fullName(userInfo.getFullName())
                .email(userInfo.getEmail())
                .phoneNumber(userInfo.getPhone())
                .areaId(userInfo.getArea() != null ? userInfo.getArea().getId() : null)
                .addressDetail(userInfo.getFullAddress())
                .avatar(userInfo.getAvatar())
                .gender(userInfo.getGender())
                .build();
    }

    @Override
    public Object getUserByConditions(GetUserInfoRequest request) {
        Long userId = 0L;
        if (request.getUserInfoId() != null) {
            userId = request.getUserInfoId();
        }
        if (StringUtils.hasText(request.getUserName())) {
            UserAuth userAuth = userAuthRepository.findByUserName(request.getUserName())
                    .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_EXISTED));
            userId = userAuth.getUserInfoId();
        }
        UserInfoResponse userCache = redisService.hget(RedisConstant.Htable.USER_INFO,
                Long.toString(userId), UserInfoResponse.class);
        if (userCache == null) {
            UserInfo userInfo = findUserInfoById(userId);
            try {
                redisService.hset(RedisConstant.Htable.USER_INFO,
                        Long.toString(userId),
                        objectMapper.writeValueAsString(userInfo),
                        null);
            } catch (JsonProcessingException e) {
                log.error("Error while caching user info to Redis", e);
            }
            userCache = UserInfoResponse.builder()
                    .id(userInfo.getId())
                    .userNo(userInfo.getUserNo())
                    .fullName(userInfo.getFullName())
                    .email(userInfo.getEmail())
                    .phoneNumber(userInfo.getPhone())
                    .areaId(userInfo.getArea() != null ? userInfo.getArea().getId() : null)
                    .addressDetail(userInfo.getFullAddress())
                    .avatar(userInfo.getAvatar())
                    .gender(userInfo.getGender())
                    .build();
        }
        return userCache;
    }

    private UserInfo findUserInfoById(Long userId) {
        return userInfoRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_EXISTED));
    }


}

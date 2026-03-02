package com.example.postservice.service;

import com.example.commericalcommon.dto.response.HashtagResponse;
import com.example.commericalcommon.dto.response.user.UserInfoResponse;
import com.example.commericalcommon.enums.ObjectType;

import java.util.List;

public interface HashtagService {
    List<HashtagResponse> getHashtagsByCondition(Object request);

    void insertHashtags(List<String> hashtag, ObjectType objectType, Long objectId, UserInfoResponse userInfo);
}

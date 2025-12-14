package com.example.postservice.service;

import com.example.commericalcommon.dto.response.HashtagResponse;

import java.util.List;

public interface HashtagService {
    List<HashtagResponse> getHashtagsByCondition(Object request);

    void addHashtags(String hashtag, String objectType, Long objectId);
}

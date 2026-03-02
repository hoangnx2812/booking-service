package com.example.postservice.service.impl;

import com.example.commericalcommon.dto.object.HashtagsDTO;
import com.example.commericalcommon.dto.response.HashtagResponse;
import com.example.commericalcommon.dto.response.user.UserInfoResponse;
import com.example.commericalcommon.enums.ObjectType;
import com.example.postservice.repository.jdbc.HashtagRepositoryJdbc;
import com.example.postservice.service.HashtagService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class HashtagServiceImpl implements HashtagService {
    HashtagRepositoryJdbc hashtagRepositoryJdbc;

    @Override
    public List<HashtagResponse> getHashtagsByCondition(Object request) {
        return List.of();
    }

    @Override
    public void insertHashtags(List<String> hashtag, ObjectType objectType, Long objectId, UserInfoResponse userInfo) {
        List<HashtagsDTO> hashTagExists = hashtagRepositoryJdbc.getHashTagByName(hashtag);
        List<String> hashTagNotExists = hashtag.stream()
                .filter(name ->
                        hashTagExists.stream().noneMatch(ht -> ht.getName().equalsIgnoreCase(name)))
                .toList();
        if (!CollectionUtils.isEmpty(hashTagExists)) {
            int batchSize = 100;
            for (int i = 0; i < hashTagExists.size(); i += batchSize) {
                int end = Math.min(i + batchSize, hashTagExists.size());
                List<HashtagsDTO> batch = hashTagExists.subList(i, end);
                hashtagRepositoryJdbc.insertHashtagMap(batch.stream().map(HashtagsDTO::getId).toList(), objectId, objectType);
            }
        }
        if (!CollectionUtils.isEmpty(hashTagNotExists)) {
            hashTagNotExists.forEach(name -> {
                Long hashtagId = hashtagRepositoryJdbc.insertHashtag(name);
                hashtagRepositoryJdbc.insertHashtagMap(hashtagId, objectId, objectType);
            });
        }
    }
}

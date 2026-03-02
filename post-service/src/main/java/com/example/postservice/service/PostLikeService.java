package com.example.postservice.service;

import com.example.postservice.dto.request.LikeActionRequest;

public interface PostLikeService {
    void likeActionPost(LikeActionRequest request);

    Object getPostLikeByConditions(Object postLike);
}

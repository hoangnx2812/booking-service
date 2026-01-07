package com.example.postservice.service;

import com.example.commericalcommon.dto.PageResponse;
import com.example.postservice.dto.request.GetPostRequest;
import com.example.postservice.dto.request.InsertPostRequest;
import com.example.postservice.dto.response.GetPostsResponse;

public interface PostService {

    PageResponse<GetPostsResponse> getPostsByConditions(GetPostRequest request);

    void insertPost(InsertPostRequest request);

    Object deletePost(Object post);

    Object updatePost(Object post);
}

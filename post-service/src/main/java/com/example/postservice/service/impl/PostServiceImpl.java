package com.example.postservice.service.impl;

import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.dto.PageResponse;
import com.example.commericalcommon.dto.request.GetUserInfoRequest;
import com.example.commericalcommon.dto.response.user.UserInfoResponse;
import com.example.commericalcommon.enums.AttachmentFolder;
import com.example.commericalcommon.enums.ObjectType;
import com.example.commericalcommon.exception.ErrorCode;
import com.example.commericalcommon.exception.GlobalException;
import com.example.postservice.dto.request.GetPostRequest;
import com.example.postservice.dto.request.InsertPostRequest;
import com.example.postservice.dto.response.GetPostsResponse;
import com.example.postservice.entity.Post;
import com.example.postservice.repository.PostServiceRepository;
import com.example.postservice.repository.PostsRepository;
import com.example.postservice.repository.httpclient.AuthenticationClient;
import com.example.postservice.repository.jdbc.PostsRepositoryJdbc;
import com.example.postservice.service.AttachmentService;
import com.example.postservice.service.HashtagService;
import com.example.postservice.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.commericalcommon.utils.Constant.SUCCESS_CODE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostServiceImpl implements PostService {
    PostsRepositoryJdbc postRepositoryJdbc;
    AttachmentService attachmentService;
    HashtagService hashtagService;
    PostServiceRepository postServiceRepository;
    PostsRepository postsRepository;
    AuthenticationClient authenticationClient;

    @Override
    public PageResponse<GetPostsResponse> getPostsByConditions(GetPostRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
        return PageResponse.<GetPostsResponse>builder()
                .content(postRepositoryJdbc.getPostsByConditions(request, offset))
                .currentPage(request.getPage())
                .pageSize(request.getSize())
                .build();
    }

    @Override
    @Transactional
    public void insertPost(InsertPostRequest request) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BaseResponse<UserInfoResponse> userInfo =
                authenticationClient.getUserByConditions(GetUserInfoRequest.builder()
                        .userName(userName)
                        .build());
        log.info("Response from authentication-service: {}", userInfo);
        if (!SUCCESS_CODE.equals(userInfo.getResultCode())) {
            throw new GlobalException(ErrorCode.SESSION_EXPIRED);
        }

        Post post = new Post();
        post.setTitle(request.getContent());
        post.setUserInfoId(userInfo.getData().getId());
        post.setServicesIds(request.getServiceIds());
        postsRepository.save(post);

        attachmentService.insertAttachment(request.getAttachments(), AttachmentFolder.POST_IMAGE, ObjectType.POST,
                post.getId(), userInfo.getData());

        hashtagService.insertHashtags(request.getHashtags(), ObjectType.POST, post.getId(), userInfo.getData());
    }

    @Override
    public Object deletePost(Object post) {
        return null;
    }

    @Override
    public Object updatePost(Object post) {
        return null;
    }
}

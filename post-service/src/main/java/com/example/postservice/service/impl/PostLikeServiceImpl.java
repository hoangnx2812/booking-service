package com.example.postservice.service.impl;

import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.dto.request.GetUserInfoRequest;
import com.example.commericalcommon.dto.response.user.UserInfoResponse;
import com.example.commericalcommon.exception.ErrorCode;
import com.example.commericalcommon.exception.GlobalException;
import com.example.postservice.dto.request.LikeActionRequest;
import com.example.postservice.entity.Post;
import com.example.postservice.entity.PostLike;
import com.example.postservice.repository.PostLikeRepository;
import com.example.postservice.repository.PostsRepository;
import com.example.postservice.repository.httpclient.AuthenticationClient;
import com.example.postservice.service.PostLikeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.example.commericalcommon.utils.Constant.SUCCESS_CODE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {
    PostLikeRepository postLikeRepository;
    AuthenticationClient authenticationClient;
    PostsRepository postsRepository;

    @Override
    public void likeActionPost(LikeActionRequest request) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        BaseResponse<UserInfoResponse> userInfo =
                authenticationClient.getUserByConditions(GetUserInfoRequest.builder()
                        .userName(userName)
                        .build());
        log.info("Response from authentication-service: {}", userInfo);

        if (!SUCCESS_CODE.equals(userInfo.getResultCode())) {
            throw new GlobalException(ErrorCode.SESSION_EXPIRED);
        }

        if (Boolean.TRUE.equals(request.getIsLike())) {
            Post post = postsRepository.findById(request.getPostId())
                    .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));

            PostLike postLike = new PostLike();
            postLike.setPost(post);
            postLike.setUserId(userInfo.getData().getId());
            postLikeRepository.save(postLike);
        } else {
            postLikeRepository.deleteByPostIdAndUserId(request.getPostId(), userInfo.getData().getId());
        }
    }


    @Override
    public Object getPostLikeByConditions(Object postLike) {
        return null;
    }
}

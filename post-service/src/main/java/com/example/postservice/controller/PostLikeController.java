package com.example.postservice.controller;

import com.example.commericalcommon.dto.BaseRequest;
import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.utils.MessageUtil;
import com.example.postservice.dto.request.LikeActionRequest;
import com.example.postservice.service.PostLikeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post/like")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostLikeController {
    PostLikeService postLikeService;
    MessageUtil messageUtil;

    @PostMapping("/action")
    public BaseResponse<Void> likeActionPost(@Valid @RequestBody BaseRequest<LikeActionRequest> baseRequest) {
        long startTime = System.currentTimeMillis();
        postLikeService.likeActionPost(baseRequest.getData());
        return BaseResponse.<Void>builder()
                .requestId(baseRequest.getRequestId())
                .requestTime(baseRequest.getClientRequestId())
                .resultDesc(messageUtil.getMessage("CREATE_SUCCESS"))
                .cost(System.currentTimeMillis() - startTime)
                .build();
    }
}

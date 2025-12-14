package com.example.postservice.repository.jdbc;

import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.dto.object.HashtagsDTO;
import com.example.commericalcommon.dto.object.ServicesDTO;
import com.example.commericalcommon.dto.request.GetUserInfoRequest;
import com.example.commericalcommon.dto.response.user.UserInfoResponse;
import com.example.commericalcommon.service.RedisService;
import com.example.commericalcommon.utils.Constant;
import com.example.commericalcommon.utils.DateTimeFormatter;
import com.example.commericalcommon.utils.RedisConstant;
import com.example.postservice.dto.request.GetPostRequest;
import com.example.postservice.dto.response.GetPostsResponse;
import com.example.postservice.repository.PostCommentRepository;
import com.example.postservice.repository.PostLikeRepository;
import com.example.postservice.repository.httpclient.AuthenticationClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.commericalcommon.utils.Constant.SUCCESS_CODE;
import static com.example.commericalcommon.utils.Util.isNotNull;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostsRepositoryJdbc {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    ServicesRepositoryJdbc servicesRepositoryJdbc;
    HashtagRepositoryJdbc hashtagRepositoryJdbc;
    PostCommentRepository postCommentRepository;
    PostLikeRepository postLikeRepository;
    AttachmentRepositoryJdbc attachmentRepositoryJdbc;
    DateTimeFormatter dateTimeFormatter;
    AuthenticationClient authenticationClient;
    RedisService redisService;
    ObjectMapper objectMapper;

    public List<GetPostsResponse> getPostsByConditions(GetPostRequest request, int offset) {
        StringBuilder sql = new StringBuilder("""
                select p.id, p.title, p.services_ids, p.created_at, p.user_id
                from posts p
                where 1 = 1
                and p.is_active = true
                """);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        String keyword = request.getKeyword();
        if (StringUtils.hasText(keyword)) {
            List<HashtagsDTO> hashtags = hashtagRepositoryJdbc.getHashtagsByConditions(
                    null,
                    Constant.Hashtag.ObjectType.POST,
                    keyword);

            if (CollectionUtils.isEmpty(hashtags)) {
                sql.append("""
                        and (p.title = :keyword)
                        """);
            } else {
                sql.append("""
                        and (p.title = :keyword or p.id in (:post_ids))
                        """);
                sqlParameterSource.addValue("post_ids", hashtags.stream().map(HashtagsDTO::getObjectId).toList());
            }
            sqlParameterSource.addValue("keyword", keyword);
        }
        if (request.getPriceFrom() != null && request.getPriceTo() != null) {
            List<ServicesDTO> services = servicesRepositoryJdbc.getServicesByConditions(
                    null,
                    request.getPriceFrom(),
                    request.getPriceTo(),
                    null,
                    null);
            if (!CollectionUtils.isEmpty(services)) {
                sql.append("""
                        and services_ids in (:service_ids)
                        """);
                sqlParameterSource.addValue("service_ids", services.stream().map(ServicesDTO::getId).toList());
            }
        }
        sql.append(" order by p.created_at desc ");
        sql.append(" limit :size offset :offset ");
        sqlParameterSource.addValue("size", request.getSize());
        sqlParameterSource.addValue("offset", offset);
        return namedParameterJdbcTemplate.query(sql.toString(), sqlParameterSource, (rs, rowNum) ->
        {
            Long id = rs.getLong("id");
            long userInfoId = rs.getLong("user_id");
            UserInfoResponse userCache = redisService.hget(RedisConstant.Htable.USER_INFO,
                    Long.toString(userInfoId), UserInfoResponse.class);
            if (userCache == null) {
                BaseResponse<UserInfoResponse> userInfo =
                        authenticationClient.getUserByConditions(GetUserInfoRequest.builder()
                                .userInfoId(userInfoId)
                                .build());
                log.info("Response from authentication-service: {}", userInfo);
                if (!SUCCESS_CODE.equals(userInfo.getResultCode())) {
                    return null;
                }
                try {
                    redisService.hset(RedisConstant.Htable.USER_INFO,
                            Long.toString(userInfoId),
                            objectMapper.writeValueAsString(userInfo.getData()),
                            RedisConstant.Htable.TTL_DEFAULT);
                } catch (JsonProcessingException e) {
                    log.error("Error while caching user info to Redis", e);
                }
                userCache = userInfo.getData();
            }
            return GetPostsResponse.builder()
                    .postId(id)
                    .userInfoId(userInfoId)
                    .userAvatar(userCache.getAvatar())
                    .userFullName(userCache.getFullName())
                    .postTitle(rs.getString("title"))
                    .serviceName(servicesRepositoryJdbc.getServicesByConditions(null, null,
                                    null, null,
                                    List.of(rs.getLong("services_ids")))
                            .stream().map(ServicesDTO::getName).toList())
                    .createdAt("Được đăng vào " +
                            dateTimeFormatter.format(isNotNull(rs.getTimestamp("created_at"))))
                    .totalComments(String.valueOf(postCommentRepository.countByPosts_Id(id)))
                    .totalLikes(String.valueOf(postLikeRepository.countByPosts_Id(id)))
                    .attachments(attachmentRepositoryJdbc.getAllAttachments(id,
                            Constant.Attachment.ObjectType.POST))
                    .build();
        });
    }
}

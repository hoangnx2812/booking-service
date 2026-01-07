package com.example.postservice.repository.jdbc;

import com.example.commericalcommon.dto.object.HashtagsDTO;
import com.example.commericalcommon.enums.ObjectType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class HashtagRepositoryJdbc {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<HashtagsDTO> getHashtagsByConditions(Long objectId, String objectType, String name) {
        StringBuilder sql = new StringBuilder("""
                select hm.id, h.hashtag, hm.object_type, hm.object_id
                from hashtags h join hashtags_map hm on hashtags.id = hm.hashtag_id
                where 1 = 1
                """);
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (objectId != null) {
            sql.append(" and object_id = :object_id ");
            params.addValue("object_id", objectId);
        }
        if (StringUtils.hasText(name)) {
            sql.append(" and hashtag = :hashtag ");
            params.addValue("hashtag", name);
        }
        if (StringUtils.hasText(objectType)) {
            sql.append(" and object_type = :object_type ");
            params.addValue("object_type", objectType);
        }
        return namedParameterJdbcTemplate.query(sql.toString(), params, (rs, rowNum) ->
                HashtagsDTO.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("hashtag"))
                        .objectType(rs.getString("object_type"))
                        .objectId(rs.getLong("object_id"))
                        .build());
    }

    public HashtagsDTO getHashTagByConditions(Long id, String name) {
        String sql = """
                select * from hashtags where 1 = 1
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (StringUtils.hasText(name)) {
            sql += " and hashtag = :hashtag ";
            params.addValue("hashtag", name);
        }
        if (id != null) {
            sql += " and id = :id ";
            params.addValue("id", id);
        }
        List<HashtagsDTO> hashtagsDTOS = namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) ->
                HashtagsDTO.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("hashtag"))
                        .build());
        return CollectionUtils.isEmpty(hashtagsDTOS) ? null : hashtagsDTOS.getFirst();
    }

    public List<HashtagsDTO> getHashTagByName(List<String> name) {
        String sql = """
                select * from hashtags where 1 = 1 and hashtag in (:hashtag)
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("hashtag", name);
        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) ->
                HashtagsDTO.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("hashtag"))
                        .build());
    }

    public void insertHashtagMap(Long hashtagId, Long objectId, ObjectType objectType) {
        String sql = """
                insert into hashtags_map (hashtag_id, object_id, object_type, hashtags_type)
                values (:hashtag_id, :object_id, :object_type, :hashtags_type)
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("hashtag_id", hashtagId);
        params.addValue("object_id", objectId);
        params.addValue("object_type", objectType.getType());
        params.addValue("hashtags_type", objectType.getType());
        namedParameterJdbcTemplate.update(sql, params);
    }

    public void insertHashtagMap(List<Long> hashtagId, Long objectId, ObjectType objectType) {
        StringBuilder sql = new StringBuilder("""
                insert into hashtags_map (hashtag_id, object_id, object_type, hashtags_type)
                values
                """);
        MapSqlParameterSource params = new MapSqlParameterSource();
        for (int i = 0; i < hashtagId.size(); i++) {
            sql.append(" (:hashtag_id").append(i)
                    .append(", :object_id").append(i)
                    .append(", :object_type").append(i)
                    .append(", :hashtags_type").append(i).append(") ");
            if (i < hashtagId.size() - 1) {
                sql.append(", ");
            }
            params.addValue("hashtag_id" + i, hashtagId.get(i));
            params.addValue("object_id" + i, objectId);
            params.addValue("object_type" + i, objectType.getType());
            params.addValue("hashtags_type" + i, objectType.getType());
        }
        namedParameterJdbcTemplate.update(sql.toString(), params);
    }

    public Long insertHashtag(String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
                insert into hashtags (hashtag)
                values (:hashtag)
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("hashtag", name);
        namedParameterJdbcTemplate.update(sql, params);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}

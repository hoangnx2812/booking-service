package com.example.postservice.repository.jdbc;

import com.example.commericalcommon.dto.object.AttachmentDTO;
import com.example.commericalcommon.dto.object.AttachmentMapDTO;
import com.example.commericalcommon.dto.response.AttachmentResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AttachmentRepositoryJdbc {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<AttachmentResponse> getAllAttachments(Long objectId, String objectType) {
        String sql = """
                select am.id,
                       am.display_name,
                       a.mime_type,
                       a.size,
                       a.thumbnail,
                       a.file_path_sm,
                       a.file_path_lg,
                       a.file_path_original
                from attachment_map am
                         join attachment a on am.attachment_id = a.id
                where object_id = :object_id
                  and object_type = :object_type
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("object_id", objectId)
                .addValue("object_type", objectType);
        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) ->
                AttachmentResponse.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .thumbnail(rs.getString("thumbnail"))
                        .pathSmall(rs.getString("path_small"))
                        .pathLarge(rs.getString("path_large"))
                        .pathOriginal(rs.getString("path_original"))
                        .size(rs.getString("size"))
                        .type(rs.getString("type"))
                        .build());
    }

    public List<AttachmentDTO> checkSumExists(List<String> checkSum) {
        String sql = """
                select id, file_name, checksum
                from attachment
                where checksum in (:checksum)
                and status = 'A'
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("checksum", checkSum);
        return namedParameterJdbcTemplate.query(sql, params,
                (rs, rowNum) ->
                        AttachmentDTO.builder()
                                .id(rs.getLong("id"))
                                .fileName(rs.getString("name"))
                                .checksum(rs.getString("checksum"))
                                .build());
    }

    public void insertAttachmentMap(AttachmentMapDTO request) {
        String sql = """
                INSERT INTO attachment_map (
                                code,
                                display_name,
                                object_type,
                                object_id,
                                client_upload_code,
                                description,
                                created_by,
                                status,
                                order_no,
                                attachment_id
                            ) VALUES (
                                :code,
                                :displayName,
                                :objectType,
                                :objectId,
                                :clientUploadCode,
                                :description,
                                :createdBy,
                                :status,
                                :orderNo,
                                :attachmentId
                            )
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("code", request.getCode())
                .addValue("displayName", request.getDisplayName())
                .addValue("objectType", request.getObjectType())
                .addValue("objectId", request.getObjectId())
                .addValue("clientUploadCode", request.getClientUploadCode())
                .addValue("description", request.getDescription())
                .addValue("createdBy", request.getCreatedBy())
                .addValue("status", request.getStatus())
                .addValue("orderNo", request.getOrderNo())
                .addValue("attachmentId", request.getAttachmentId());
        namedParameterJdbcTemplate.update(sql, params);
    }

    public Long insertAttachment(AttachmentDTO dto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
                INSERT INTO attachment (
                    mime_type,
                    file_name,
                    thumbnail,
                    file_path_sm,
                    file_path_lg,
                    file_path_original,
                    checksum,
                    size,
                    description,
                    created_by,
                    folder,
                    source,
                    is_sync_vultr,
                    is_sync_vultr_error
                ) VALUES (
                    :mimeType,
                    :fileName,
                    :thumbnail,
                    :filePathSm,
                    :filePathLg,
                    :filePathOriginal,
                    :checksum,
                    :size,
                    :description,
                    :createdBy,
                    :folder,
                    :source,
                    :isSyncVultr,
                    :isSyncVultrError
                )
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("mimeType", dto.getMimeType())
                .addValue("fileName", dto.getFileName())
                .addValue("thumbnail", dto.getThumbnail())
                .addValue("filePathSm", dto.getFilePathSm())
                .addValue("filePathLg", dto.getFilePathLg())
                .addValue("filePathOriginal", dto.getFilePathOriginal())
                .addValue("checksum", dto.getChecksum())
                .addValue("size", dto.getSize())
                .addValue("description", dto.getDescription())
                .addValue("createdBy", dto.getCreatedBy())
                .addValue("folder", dto.getFolder() != null ? dto.getFolder() : "uploads")
                .addValue("source", dto.getSource() != null ? dto.getSource() : "s3")
                .addValue("isSyncVultr", dto.getIsSyncVultr() != null ? dto.getIsSyncVultr() : false)
                .addValue("isSyncVultrError", dto.getIsSyncVultrError() != null ? dto.getIsSyncVultrError() : false);

        namedParameterJdbcTemplate.update(sql, params);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }


}

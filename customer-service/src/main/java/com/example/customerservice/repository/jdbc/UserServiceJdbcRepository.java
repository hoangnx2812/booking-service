package com.example.customerservice.repository.jdbc;

import com.example.commericalcommon.dto.object.UserServicesDTO;
import com.example.commericalcommon.dto.request.GetUserServiceRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceJdbcRepository {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<UserServicesDTO> getServicesByConditions(GetUserServiceRequest request) {
        StringBuilder sql = new StringBuilder("""
                select us.id  as user_services_id,
                       usm.id as user_services_map_id,
                       us.title,
                       us.price,
                       us.duration_time,
                       usm.object_id,
                       usm.object_type
                from user_services us
                         join user_services_map usm on us.id = usm.user_services_id
                where 1 = 1
                """);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if (StringUtils.hasText(request.getTitle())) {
            sql.append(" and us.title = :title ");
            parameterSource.addValue("title", "%" + request.getTitle() + "%");
        }
        if (request.getPriceFrom() != null && request.getPriceTo() != null) {
            sql.append(" and us.price between :priceFrom and :priceTo ");
            parameterSource.addValue("priceFrom", request.getPriceFrom());
            parameterSource.addValue("priceTo", request.getPriceTo());
        }
        if (StringUtils.hasText(request.getDuration())) {
            sql.append(" and us.duration_time = :duration ");
            parameterSource.addValue("duration", request.getDuration());
        }
        if (StringUtils.hasText(request.getObjectId()) && StringUtils.hasText(request.getObjectType())) {
            sql.append(" and usm.object_id = :objectId and usm.object_type = :objectType ");
            parameterSource.addValue("objectId", request.getObjectId());
            parameterSource.addValue("objectType", request.getObjectType());

        }

        sql.append(" order by usm.created_at desc ");
        return namedParameterJdbcTemplate.query(sql.toString(), parameterSource, (rs, rowNum) ->
                UserServicesDTO.builder()
                        .userServiceId(rs.getLong("user_services_id"))
                        .userServiceMapId(rs.getLong("user_services_map_id"))
                        .name(rs.getString("title"))
                        .price(rs.getDouble("price"))
                        .time(rs.getString("duration_time"))
                        .objectId(rs.getLong("object_id"))
                        .objectType(rs.getString("object_type"))
                        .build()
        );
    }
}

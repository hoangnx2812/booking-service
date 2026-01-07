package com.example.authenservice.entity;

import com.example.commericalcommon.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "user_auth")
public class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @NotNull
    @Column(name = "user_name", nullable = false, length = Integer.MAX_VALUE)
    String userName;

    @Column(name = "user_info_id")
    Long userInfoId;

    @Column(name = "user_info_no", length = Integer.MAX_VALUE)
    String userInfoNo;

    @Column(name = "user_pwd_hash", length = Integer.MAX_VALUE)
    String userPwdHash;

    @Column(name = "user_salt", length = Integer.MAX_VALUE)
    String userSalt;

    @Column(name = "device_id", length = Integer.MAX_VALUE)
    String deviceId;

    @Column(name = "number_of_device")
    Integer numberOfDevice;

    @Column(name = "platform_version", length = Integer.MAX_VALUE)
    String platformVersion;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "status", length = Integer.MAX_VALUE)
    String status;

    @Column(name = "keycloak_id", length = Integer.MAX_VALUE)
    String keycloakId;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = Status.ACTIVE.getStatus();
    }

}
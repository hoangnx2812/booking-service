package com.example.authenservice.entity;

import com.example.commericalcommon.enums.Status;
import com.example.commericalcommon.utils.Constant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_auth_id", nullable = false)
    UserAuth userAuth;

    @Size(max = 45)
    @Column(name = "user_no", length = 45)
    String userNo;

    @Size(max = 255)
    @Column(name = "full_name")
    String fullName;

    @Size(max = 255)
    @Column(name = "first_name")
    String firstName;

    @Size(max = 255)
    @Column(name = "middle_name")
    String middleName;

    @Size(max = 255)
    @Column(name = "last_name")
    String lastName;

    @Size(max = 255)
    @Column(name = "display_name")
    String displayName;

    @Column(name = "birthday")
    LocalDate birthday;

    @Size(max = 255)
    @Column(name = "email")
    String email;

    @Size(max = 255)
    @Column(name = "phone")
    String phone;

    @Size(max = 1000)
    @Column(name = "avatar", length = 1000)
    String avatar;

    @Size(max = 45)
    @Column(name = "gender", length = 45)
    String gender;

    @Size(max = 255)
    @Column(name = "address_detail")
    String addressDetail;

    @Size(max = 300)
    @Column(name = "full_address", length = 300)
    String fullAddress;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    String description;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Size(max = 45)
    @Column(name = "status", length = 45)
    String status;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "enable_notification", nullable = false)
    Boolean enableNotification;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "enable_email_notification", nullable = false)
    Boolean enableEmailNotification;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "enable_sms", nullable = false)
    Boolean enableSms;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "enable_promotions_notification", nullable = false)
    Boolean enablePromotionsNotification;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "enable_promotions_email_notification", nullable = false)
    Boolean enablePromotionsEmailNotification;

    @NotNull
    @ColumnDefault("'vi'")
    @Column(name = "language", nullable = false, length = Integer.MAX_VALUE)
    String language;

    @Column(name = "latitude")
    Double latitude;

    @Column(name = "longitude")
    Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Area area;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = Status.ACTIVE.getStatus();
        enableNotification = Boolean.TRUE;
        enableEmailNotification = Boolean.FALSE;
        enableSms = Boolean.FALSE;
        enablePromotionsNotification = Boolean.TRUE;
        enablePromotionsEmailNotification = Boolean.TRUE;
        language = Constant.Language.VIETNAMESE;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

/*
 TODO [Reverse Engineering] create field to map the 'location' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "location", columnDefinition = "geography")
     Object location;
*/
}
package com.example.postservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "title", length = Integer.MAX_VALUE)
    private String title;

    @Column(name = "user_info_id")
    private Long userInfoId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "services_ids")
    private List<Long> servicesIds;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "adv_navigate_type", length = Integer.MAX_VALUE)
    private String advNavigateType;

    @Column(name = "adv_link", length = Integer.MAX_VALUE)
    private String advLink;

    @Column(name = "adv_keyword", length = Integer.MAX_VALUE)
    private String advKeyword;

    @ColumnDefault("false")
    @Column(name = "enable_sub_title_adv")
    private Boolean enableSubTitleAdv;

    @Column(name = "adv_index")
    private Integer advIndex;

    @Column(name = "adv_search_sort", length = Integer.MAX_VALUE)
    private String advSearchSort;

    @Column(name = "adv_label_btn", length = Integer.MAX_VALUE)
    private String advLabelBtn;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "adv_target_id")
    private Long advTargetId;

    @ColumnDefault("1")
    @Column(name = "priority")
    private Integer priority;

    @Column(name = "min_price_sort")
    private Integer minPriceSort;

    @Column(name = "max_price_sort")
    private Integer maxPriceSort;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        isActive = Boolean.TRUE;
        enableSubTitleAdv = Boolean.FALSE;
        priority = 1;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
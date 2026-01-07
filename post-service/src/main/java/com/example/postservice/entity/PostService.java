package com.example.postservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "post_services")
public class PostService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "title", length = Integer.MAX_VALUE)
    private String title;

    @ColumnDefault("0")
    @Column(name = "price")
    private BigDecimal price;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "duration_time", length = Integer.MAX_VALUE)
    private String durationTime;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ColumnDefault("0")
    @Column(name = "sort")
    private BigDecimal sort;

    @Column(name = "deleted_at")
    private Instant deletedAt;


}
package com.example.customerservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_services")
public class UserService {
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

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


}
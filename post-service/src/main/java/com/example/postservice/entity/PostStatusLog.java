package com.example.postservice.entity;

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
@Table(name = "post_status_logs")
public class PostStatusLog {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "updated_by_id")
    private Long updatedById;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plans plan;

    @Column(name = "remaining_plan_day")
    private BigDecimal remainingPlanDay;

    @Column(name = "remaining_plan_minutes")
    private BigDecimal remainingPlanMinutes;

    @Column(name = "remaining_plan_unit", length = Integer.MAX_VALUE)
    private String remainingPlanUnit;

    @Column(name = "post_status_logs_type", length = Integer.MAX_VALUE)
    private String postStatusLogsType;

    @Column(name = "note", length = Integer.MAX_VALUE)
    private String note;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
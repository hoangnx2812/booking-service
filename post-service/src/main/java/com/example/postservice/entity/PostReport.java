package com.example.postservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "post_reports")
public class PostReport {
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

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "reason", length = Integer.MAX_VALUE)
    private String reason;

    @Column(name = "post_reports_content", length = Integer.MAX_VALUE)
    private String postReportsContent;

    @Column(name = "post_reports_type", length = Integer.MAX_VALUE)
    private String postReportsType;

    @Column(name = "comment_id")
    private Long commentId;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
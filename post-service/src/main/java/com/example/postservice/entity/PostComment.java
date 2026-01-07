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
@Table(name = "post_comments")
public class PostComment {
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

    @Column(name = "content", length = Integer.MAX_VALUE)
    private String content;

    @Column(name = "medias_ids")
    private List<Long> mediasIds;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "user_ids")
    private List<Long> userIds;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
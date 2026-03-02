package com.example.postservice.repository;

import com.example.postservice.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Integer countByPost_Id(Long id);

    void deleteByPostIdAndUserId(Long postId, Long userId);
}

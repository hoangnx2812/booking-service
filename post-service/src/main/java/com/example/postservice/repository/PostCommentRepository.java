package com.example.postservice.repository;

import com.example.postservice.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    Integer countByPost_Id(Long posts_id);
}

package com.roadmaker.v1.comment.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Page<Comment> findCommentByRoadmapId(Long roadmapId, Pageable pageable);
    public Page<Comment> findCommentByMemberId(Long memberId, Pageable pageable);

    public Page<Comment> findByIdGreaterThanAndRoadmapId(Long id, Long roadmapId, Pageable pageable);

}

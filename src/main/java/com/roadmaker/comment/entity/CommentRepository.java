package com.roadmaker.comment.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Page<Comment> findCommentByRoadmapId(Long roadmapId, PageRequest pageRequest);
    public Page<Comment> findCommentByMemberId(Long memberId, Pageable pageable);

}

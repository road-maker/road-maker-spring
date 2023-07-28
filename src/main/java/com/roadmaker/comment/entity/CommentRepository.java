package com.roadmaker.comment.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findAllByRoadmapId(Long roadmapId);
    public List<Comment> findAllByMemberId(Long memberId);

}

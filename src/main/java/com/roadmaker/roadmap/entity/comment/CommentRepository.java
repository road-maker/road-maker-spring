package com.roadmaker.roadmap.entity.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findByRoadmapId(Long roadmapId);
    public List<Comment> findByMemberId(Long memberId);

}

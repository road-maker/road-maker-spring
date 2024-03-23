package com.roadmaker.v1.like.controller;

import com.roadmaker.global.annotation.LoginMember;
import com.roadmaker.global.annotation.LoginRequired;
import com.roadmaker.v1.like.dto.LikeRoadmapResponse;
import com.roadmaker.v1.like.service.LikeService;
import com.roadmaker.v1.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/likes")
@RequiredArgsConstructor
@RestController
public class LikeController {
    private final LikeService likeService;

    @LoginRequired
    @PostMapping("/like-roadmap/{roadmapId}")
    public LikeRoadmapResponse likeRoadmap(@PathVariable Long roadmapId, @LoginMember Member member) {
        return likeService.toggleLikeRoadmap(roadmapId, member);
    }

    @GetMapping("/roadmap/{roadmapId}/likeCount")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long roadmapId) {
        int likeCount = likeService.getLikeCount(roadmapId);
        return ResponseEntity.ok(likeCount);
    }
}
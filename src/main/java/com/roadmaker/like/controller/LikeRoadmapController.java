package com.roadmaker.like.controller;

import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeRoadmapController {
    private final LikeService likeService;
    @LoginRequired
    @PostMapping("/like-roadmap/{roadmapId}")
    public ResponseEntity<Boolean> likeRoadmap(@PathVariable Long roadmapId, @LoginMember Member member) {
        if (likeService.isLiked(roadmapId, member.getId())) {
            // 이미 좋아요 상태면 좋아요 취소
            likeService.unlikeRoadmap(roadmapId, member.getId());
            return ResponseEntity.ok(false);
        } else {
            // 로드맵 좋아요
            likeService.likeRoadmap(roadmapId, member.getId());
            return ResponseEntity.ok(true);
        }
    }

    // 좋아요 갯수를 알려주는 API
    @GetMapping("/roadmap/{roadmapId}/likeCount")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long roadmapId) {
        int likeCount = likeService.getLikeCount(roadmapId);
        return ResponseEntity.ok(likeCount);
    }
}

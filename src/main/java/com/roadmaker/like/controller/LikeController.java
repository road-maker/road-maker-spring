package com.roadmaker.like.controller;

import com.roadmaker.global.annotation.LoginMember;
import com.roadmaker.global.annotation.LoginRequired;
import com.roadmaker.like.dto.LikeRoadmapResponse;
import com.roadmaker.like.service.LikeService;
import com.roadmaker.member.entity.Member;
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
public class LikeController {
    private final LikeService likeService;
    @LoginRequired
    @PostMapping("/like-roadmap/{roadmapId}")
    public LikeRoadmapResponse likeRoadmap(@PathVariable Long roadmapId, @LoginMember Member member) {
        if (likeService.isLiked(roadmapId, member.getId())) {
            // 좋아요 했으면 좋아요 취소
            likeService.unlikeRoadmap(roadmapId, member.getId());
        } else {
            // 좋아요
            likeService.likeRoadmap(roadmapId, member.getId());
        }

        // 로드맵 좋아요 갯수
        int likeCount = likeService.getLikeCount(roadmapId);

        return LikeRoadmapResponse.from(likeService.isLiked(roadmapId, member.getId()), likeCount);
    }

    // 좋아요 갯수를 알려주는 API
    @GetMapping("/roadmap/{roadmapId}/likeCount")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long roadmapId) {
        int likeCount = likeService.getLikeCount(roadmapId);
        return ResponseEntity.ok(likeCount);
    }
}
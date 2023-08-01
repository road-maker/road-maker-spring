package com.roadmaker.like.controller;

import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.like.service.LikeService;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.member.service.MemberService;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmap.RoadmapRepository;
import com.roadmaker.roadmap.service.RoadmapService;
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
    public ResponseEntity<Integer> likeRoadmap(@PathVariable Long roadmapId, @LoginMember Member member) {
        if (likeService.isLiked(roadmapId, member.getId())) {
            // Unlike the roadmap
            likeService.unlikeRoadmap(roadmapId, member.getId());
        } else {
            // Like the roadmap
            likeService.likeRoadmap(roadmapId, member.getId());
        }

        // Get the updated like count
        int likeCount = likeService.getLikeCount(roadmapId);
        return ResponseEntity.ok(likeCount);
    }

    @GetMapping("/roadmap/{roadmapId}/likeCount")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long roadmapId) {
        int likeCount = likeService.getLikeCount(roadmapId);
        return ResponseEntity.ok(likeCount);
    }
}
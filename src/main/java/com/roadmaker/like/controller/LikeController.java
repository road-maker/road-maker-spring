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
    private final RoadmapRepository roadmapRepository;
    private final RoadmapService roadmapService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @LoginRequired
    @PostMapping("/click")
    public ResponseEntity<Integer> likeRoadmap(@RequestBody Long roadmapId, @LoginMember Member member) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow();

        if (likeService.isLiked(roadmapId, member.getId())) { // Call isLiked from LikeService
            // 좋아요 취소(unlike) 기능
            likeService.unlikeRoadmap(roadmapId, member.getId()); // Call unlikeRoadmap from LikeService
        } else {
            // 좋아요(like) 기능
            likeService.likeRoadmap(roadmapId, member.getId()); // Call likeRoadmap from LikeService
        }

        int likeCount = likeService.getLikeCount(roadmapId); // Call getLikeCount from LikeService
        return ResponseEntity.ok(likeCount);
    }

    @GetMapping("/roadmap/{roadmapId}/likeCount")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long roadmapId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow();
        int likeCount = roadmap.getLikeCount();
        return ResponseEntity.ok(likeCount);
    }
}
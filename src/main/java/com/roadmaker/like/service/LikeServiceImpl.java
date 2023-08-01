package com.roadmaker.like.service;

import com.roadmaker.like.entity.Like;
import com.roadmaker.like.entity.LikeRepository;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmap.RoadmapRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepository;
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;
    public LikeServiceImpl(LikeRepository likeRepository, RoadmapRepository roadmapRepository, MemberRepository memberRepository) {
        this.likeRepository = likeRepository;
        this.roadmapRepository = roadmapRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public boolean isLiked(Long roadmapId, Long memberId) {
        return likeRepository.existsByRoadmapIdAndMemberId(roadmapId, memberId);
    }

    @Transactional
    public void likeRoadmap(Long roadmapId, Long memberId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();
        Like like = new Like(roadmap, member);
        likeRepository.save(like);
    }

    @Transactional
    public void unlikeRoadmap(Long roadmapId, Long memberId) {
        likeRepository.deleteByRoadmapIdAndMemberId(roadmapId, memberId);
    }

    @Transactional
    public int getLikeCount(Long roadmapId) {
        return likeRepository.countByRoadmapId(roadmapId);
    }
}

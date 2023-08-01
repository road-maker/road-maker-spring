package com.roadmaker.like.service;

import com.roadmaker.like.entity.Like;
import com.roadmaker.like.entity.LikeRepository;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmap.RoadmapRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepository;
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;

    @Override
    public boolean isLiked(Long roadmapId, Long memberId) {
        return likeRepository.existsByRoadmapIdAndMemberId(roadmapId, memberId);
    }

    @Override
    public int getLikeCount(Long roadmapId) {
        return likeRepository.countByRoadmapId(roadmapId);
    }

    @Override
    public void toggleLike(Long roadmapId, Long memberId) {
        if (isLiked(roadmapId, memberId)) {
            unlikeRoadmap(roadmapId, memberId);
        } else {
            likeRoadmap(roadmapId, memberId);
        }
    }

    @Override
    public void likeRoadmap(Long roadmapId, Long memberId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();

        Like like = new Like(roadmap, member);
        likeRepository.save(like);
    }

    @Override
    public void unlikeRoadmap(Long roadmapId, Long memberId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();

        Like like = likeRepository.findByRoadmapAndMember(roadmap, member);
        if (like != null) {
            likeRepository.delete(like);
        }
    }
}

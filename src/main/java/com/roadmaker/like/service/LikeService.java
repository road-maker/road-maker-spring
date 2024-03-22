package com.roadmaker.like.service;

import com.roadmaker.like.dto.LikeRoadmapResponse;
import com.roadmaker.like.entity.Like;
import com.roadmaker.like.entity.LikeRepository;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmap.RoadmapRepository;
import com.roadmaker.roadmap.exception.RoadmapNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;

    public boolean isLiked(Long roadmapId, Long memberId) {
        return likeRepository.existsByRoadmapIdAndMemberId(roadmapId, memberId);
    }

    public int getLikeCount(Long roadmapId) {
        return likeRepository.countByRoadmapId(roadmapId);
    }

    @Transactional
    public LikeRoadmapResponse toggleLikeRoadmap(Long roadmapId, Member member) {

        if (isLiked(roadmapId, member.getId())) {
            likeRepository.deleteByRoadmapIdAndMemberId(roadmapId, member.getId());
            return LikeRoadmapResponse.from(false, getLikeCount(roadmapId));
        }

        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(RoadmapNotFoundException::new);

        Like like = new Like(roadmap, member);
        likeRepository.save(like);

        return LikeRoadmapResponse.from(true, getLikeCount(roadmapId));
    }
}

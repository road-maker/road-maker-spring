package com.roadmaker.roadmap.service;

import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.dto.CreateRoadmapRequest;
import org.springframework.stereotype.Service;
import com.roadmaker.roadmap.dto.RoadmapDto;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface RoadmapService {
    // 로드맵 생성
    public Long createRoadmap(CreateRoadmapRequest createRoadmapRequest, Member member);

    // 로드맵 불러오기
    public RoadmapDto findRoadmapById(Long roadmapId);

    // 참여
    public boolean joinRoadmap(Long roadmapId);

    // 진행상황 변경
    public boolean changeRoadmapStatus(Long inProgressNodeId, Boolean done);

}

package com.roadmaker.roadmap.service;

import org.springframework.stereotype.Service;
import com.roadmaker.roadmap.dto.RoadmapDto;

@Service
public interface RoadmapService {
    // 로드맵 불러오기
    public RoadmapDto findRoadmapById(Long roadmapId);

    // 참여
    public boolean joinRoadmap(Long roadmapId);

    // 진행상황 변경
    public boolean changeRoadmapStatus(Long inProgressNodeId, Boolean done);

}

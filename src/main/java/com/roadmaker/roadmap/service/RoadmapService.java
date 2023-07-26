package com.roadmaker.roadmap.service;

import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.dto.NodeStatusChangeDto;
import com.roadmaker.roadmap.dto.RoadmapRequest;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface RoadmapService {
    // 로드맵 생성
    public Long createRoadmap(RoadmapRequest roadmapRequest, Member member);

    // 로드맵 불러오기
    public Roadmap findRoadmapById(Long roadmapId);

    // 참여
    public boolean doJoinRoadmap(Long roadmapId, Member member);

    public void joinRoadmap(Roadmap roadmap, Member member);

    // 진행상황 변경
    public boolean changeRoadmapStatus(NodeStatusChangeDto nodeStatusChangeDto);

}

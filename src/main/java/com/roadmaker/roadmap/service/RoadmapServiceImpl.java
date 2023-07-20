package com.roadmaker.roadmap.service;

import com.roadmaker.roadmap.dto.RoadmapDto;
import org.springframework.stereotype.Service;

@Service
public class RoadmapServiceImpl implements RoadmapService{
    @Override
    public RoadmapDto findRoadmapById(Long roadmapId) {
        return null;
    }

    @Override
    public boolean joinRoadmap(Long roadmapId) {
        return false;
    }

    @Override
    public boolean changeRoadmapStatus(Long inProgressNodeId, Boolean done) {
        return false;
    }


}

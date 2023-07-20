package roadmap.service;

import roadmap.dto.RoadmapDto;

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

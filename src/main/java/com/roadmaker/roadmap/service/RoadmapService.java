package com.roadmaker.roadmap.service;

import com.roadmaker.image.dto.UploadImageResponse;
import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.dto.*;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public interface RoadmapService {
    // 로드맵 생성
    public Long createRoadmap(CreateRoadmapRequest createRoadmapRequest, Member member);

    public UploadImageResponse uploadThumbnail(Roadmap roadmap, MultipartFile image) throws IOException;

    // 로드맵 불러오기
    public Roadmap findRoadmapById(Long roadmapId);

    public RoadmapFindResponse findByPage(Integer page, Integer size);

    public List<RoadmapDto> findRoadmapJoinedByMemberId(Long memberId);
    public List<RoadmapDto> findRoadmapCreatedByMemberId(Long memberId);

    public void joinRoadmap(Roadmap roadmap, Member member);

    // 진행상황 변경
    public boolean changeNodeStatus(NodeStatusChangeDto nodeStatusChangeDto);

    public RoadmapFindResponse findRoadmapByKeyword(String keyword, Integer size, Integer page);

    public Boolean setBlogKeyword(BlogKeywordRequest request);
}

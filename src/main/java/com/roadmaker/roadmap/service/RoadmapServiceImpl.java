package com.roadmaker.roadmap.service;

import com.roadmaker.member.entity.Member;
import com.roadmaker.member.service.MemberService;
import com.roadmaker.roadmap.dto.CreateRoadmapRequest;
import com.roadmaker.roadmap.dto.RoadmapDto;
import com.roadmaker.roadmap.dto.RoadmapEdgeDto;
import com.roadmaker.roadmap.dto.RoadmapNodeDto;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmap.RoadmapRepository;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdge;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdgeRepository;
import com.roadmaker.roadmap.entity.roadmapeditor.RoadmapEditor;
import com.roadmaker.roadmap.entity.roadmapeditor.RoadmapEditorRepository;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNodeRepository;
import com.roadmaker.roadmap.entity.roadmapviewport.RoadmapViewport;
import com.roadmaker.roadmap.entity.roadmapviewport.RoadmapViewportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoadmapServiceImpl implements RoadmapService{

    private final MemberService memberService;
    private final RoadmapRepository roadmapRepository;
    private final RoadmapNodeRepository roadmapNodeRepository;
    private final RoadmapEdgeRepository roadmapEdgeRepository;
    private final RoadmapEditorRepository roadmapEditorRepository;
    private final RoadmapViewportRepository roadmapViewportRepository;

    @Override
    public Long createRoadmap(CreateRoadmapRequest createRoadmapRequest, Member member) {
        // roadmap 저장하기
        Roadmap roadmap = createRoadmapRequest.getRoadmap().toEntity();
        roadmapRepository.save(roadmap);

        // viewport 저장하기
        RoadmapViewport viewport = createRoadmapRequest.getViewport().toEntity();
        roadmapViewportRepository.save(viewport);


        // edge 저장하기
        List<RoadmapEdgeDto> roadmapEdgeDtos = createRoadmapRequest.getRoadmapEdges();
        List<RoadmapEdge> roadmapEdges = roadmapEdgeDtos.stream()
                .map(edgeDto -> edgeDto.toEntity(roadmap))
                .collect(Collectors.toList());
        roadmapEdgeRepository.saveAll(roadmapEdges);
//
//
//        // 노드 저장하기
        List<RoadmapNodeDto> roadmapNodeDtos = createRoadmapRequest.getRoadmapNodes();
        List<RoadmapNode> roadmapNodes = roadmapNodeDtos.stream()
                .map(nodeDto -> {
                    System.out.println("nodeDto = " + nodeDto.toString());
                    return nodeDto.toEntity(roadmap);
                })
                .collect(Collectors.toList());

        roadmapNodeRepository.saveAll(roadmapNodes);

        // 로드맵 생성자 만들기
        RoadmapEditor roadmapEditor = RoadmapEditor.builder()
                .isOwner(true)
                .member(member)
                .roadmap(roadmap)
                .build();

        roadmapEditorRepository.save(roadmapEditor);

        // roadmapId 반환
        return roadmap.getId();
    }

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

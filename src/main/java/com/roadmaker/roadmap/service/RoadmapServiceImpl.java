package com.roadmaker.roadmap.service;

import com.roadmaker.member.entity.Member;
import com.roadmaker.member.service.MemberService;
import com.roadmaker.roadmap.dto.*;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.roadmap.entity.inprogressroadmap.InProgressRoadmap;
import com.roadmaker.roadmap.entity.inprogressroadmap.InProgressRoadmapRepository;
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
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final InProgressRoadmapRepository inProgressRoadmapRepository;
    private final InProgressNodeRepository inProgressNodeRepository;

    @Override
    public Long createRoadmap(RoadmapRequest roadmapRequest, Member member) {
        // viewport 저장하기
        RoadmapViewport viewport = roadmapRequest.getViewport().toEntity();
        roadmapViewportRepository.save(viewport);

        // roadmap 저장하기
        Roadmap roadmap = roadmapRequest.getRoadmap().toEntity();
        roadmap.setRoadmapViewport(viewport);
        roadmapRepository.save(roadmap);

        // edge 저장하기
        List<RoadmapEdgeDto> roadmapEdgeDtos = roadmapRequest.getRoadmapEdges();
        List<RoadmapEdge> roadmapEdges = roadmapEdgeDtos.stream()
                .map(edgeDto -> edgeDto.toEntity(roadmap))
                .collect(Collectors.toList());
        roadmapEdgeRepository.saveAll(roadmapEdges);
//
//
//        // 노드 저장하기
        List<RoadmapNodeDto> roadmapNodeDtos = roadmapRequest.getRoadmapNodes();
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
        Optional<Roadmap> roadmapOptional = roadmapRepository.findById(roadmapId);
        Roadmap roadmap = roadmapOptional.orElse(null);
        if (roadmap != null) {
            return RoadmapDto.of(roadmap);
        }
        return null;
    }

    @Override
    public boolean doJoinRoadmap(Long roadmapId, Member member) {
        //해당 유저가 이미 join 하고 있다면 거짓 반환
        if (inProgressRoadmapRepository.findByRoadmapIdAndMemberId(roadmapId, member.getId()).isPresent()) {
            return false;
        }

        Optional<Roadmap> roadmapOptional = roadmapRepository.findById(roadmapId);
        Roadmap roadmap = roadmapOptional.orElse(null); //nul 처리는 필요는 없음

        //inProgressRoadmap 에 들어갈 inProgressNode 들의 리스트 생성
        // 리스트 검색-해당 로드맵에 해당하는 모든 노드들(모조리)
        List<RoadmapNode> roadmapNodes = roadmapNodeRepository.findByRoadmapId(roadmapId);

        // 로드맵 join 시 생성되는 InProgressNode 리스트 생성
        List<InProgressNode> inProgressNodes = new ArrayList<>();

        roadmapNodes
                .forEach(node -> {InProgressNode inProgressNode = InProgressNode.builder()
                        .roadmap(roadmap)
                        .roadmapNode(node)
                        .member(member)
                        .done(false)
                        .build();
                    inProgressNodeRepository.save(inProgressNode);
                    inProgressNodes.add(inProgressNode);
                });

        InProgressRoadmap inProgressRoadmap= InProgressRoadmap.builder()
                .roadmap(roadmap)
                .member(member)
                .inProgressNodes(inProgressNodes)
                .done(false)
                .build();

        inProgressRoadmapRepository.save(inProgressRoadmap);

        return true;
    }

    @Override
    public boolean changeRoadmapStatus(NodeStatusChangeDto nodeStatusChangeDto) {

        Long nodeId = nodeStatusChangeDto.getInProgressNodeId();
        Optional<InProgressNode> inProgressNodeOptional = inProgressNodeRepository.findById(nodeId);
        InProgressNode inProgressNode = inProgressNodeOptional.orElse(null);
        if (inProgressNode == null) {
            return false;
        }

        inProgressNode.setDone(!Boolean.TRUE.equals(nodeStatusChangeDto.getDone()));
        inProgressNodeRepository.save(inProgressNode);

        return true;
    }

}

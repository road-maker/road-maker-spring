package com.roadmaker.roadmap.service;

import com.roadmaker.commons.exception.ConflictException;
import com.roadmaker.commons.exception.NotFoundException;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<RoadmapEdgeDto> roadmapEdgeDtos = roadmapRequest.getEdges();
        List<RoadmapEdge> roadmapEdges = roadmapEdgeDtos.stream()
                .map(edgeDto -> edgeDto.toEntity(roadmap)).toList();
        roadmapEdgeRepository.saveAll(roadmapEdges);
//
//
//        // 노드 저장하기
        List<RoadmapNodeDto> roadmapNodeDtos = roadmapRequest.getNodes();
        List<RoadmapNode> roadmapNodes = roadmapNodeDtos
                .stream()
                .map(nodeDto -> nodeDto.toEntity(roadmap))
                .toList();

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
    public Roadmap findRoadmapById(Long roadmapId) {
        return roadmapRepository.findById(roadmapId).orElseThrow(NotFoundException::new);
    }

    @Override
    @Transactional
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
    @Transactional
    public void joinRoadmap(Roadmap roadmap, Member member) {
        // 이미 참여중인지 확인
        Optional<InProgressRoadmap> inProgressRoadmapOptional = inProgressRoadmapRepository.findByRoadmapIdAndMemberId(roadmap.getId(), member.getId());
        if (inProgressRoadmapOptional.isPresent()) {
            throw new ConflictException();
        }
        
        // InProgresRoadmap 생성
        InProgressRoadmap inProgressRoadmap = InProgressRoadmap.builder()
                .roadmap(roadmap)
                .member(member)
                .done(false)
                .build();

        inProgressRoadmapRepository.save(inProgressRoadmap);

        // InProgressNode 생성
        List<RoadmapNode> roadmapNodes = roadmapNodeRepository.findByRoadmapId(roadmap.getId());

        roadmapNodes
                .forEach(node -> {InProgressNode inProgressNode = InProgressNode.builder()
                        .roadmap(roadmap)
                        .roadmapNode(node)
                        .member(member)
                        .inProgressRoadmap(inProgressRoadmap)
                        .done(false)
                        .build();
                    inProgressNodeRepository.save(inProgressNode);
                });
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

package com.roadmaker.roadmap.service;

import com.roadmaker.commons.exception.ConflictException;
import com.roadmaker.commons.exception.NotFoundException;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.roadmap.dto.*;
import com.roadmaker.comment.entity.CommentRepository;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.inprogressroadmap.entity.InProgressRoadmap;
import com.roadmaker.inprogressroadmap.entity.InProgressRoadmapRepository;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmap.RoadmapRepository;
import com.roadmaker.roadmap.entity.roadmap.RoadmapRepositoryCustom;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdge;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdgeRepository;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNodeRepository;
import com.roadmaker.roadmap.entity.roadmapviewport.RoadmapViewport;
import com.roadmaker.roadmap.entity.roadmapviewport.RoadmapViewportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoadmapServiceImpl implements RoadmapService{

    private final RoadmapRepository roadmapRepository;
    private final RoadmapNodeRepository roadmapNodeRepository;
    private final RoadmapEdgeRepository roadmapEdgeRepository;
    private final RoadmapViewportRepository roadmapViewportRepository;
    private final InProgressRoadmapRepository inProgressRoadmapRepository;
    private final InProgressNodeRepository inProgressNodeRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long createRoadmap(CreateRoadmapRequest createRoadmapRequest, Member member) {
        // viewport 저장하기
        RoadmapViewport viewport = createRoadmapRequest.getViewport().toEntity();
        roadmapViewportRepository.save(viewport);

        // roadmap 저장하기
        Roadmap roadmap = createRoadmapRequest.getRoadmap().toEntity(member);
        roadmap.setRoadmapViewport(viewport);
        roadmapRepository.save(roadmap);

        // edge 저장하기
        List<CreateRoadmapRequest.RoadmapEdgeDto> roadmapEdgeDtos = createRoadmapRequest.getEdges();
        List<RoadmapEdge> roadmapEdges = roadmapEdgeDtos.stream()
                .map(edgeDto -> edgeDto.toEntity(roadmap)).toList();
        roadmapEdgeRepository.saveAll(roadmapEdges);

//        // 노드 저장하기
        List<CreateRoadmapRequest.RoadmapNodeDto> roadmapNodeDtos = createRoadmapRequest.getNodes();
        List<RoadmapNode> roadmapNodes = roadmapNodeDtos
                .stream()
                .map(nodeDto -> nodeDto.toEntity(roadmap))
                .toList();

        roadmapNodeRepository.saveAll(roadmapNodes);

        // roadmapId 반환
        return roadmap.getId();
    }

    @Override
    public Roadmap findRoadmapById(Long roadmapId) {
        return roadmapRepository.findById(roadmapId).orElseThrow(NotFoundException::new);
    }

    public List<RoadmapDto> findRoadmapJoinedByMemberId(Long memberId){

         List<InProgressRoadmap> inProgressRoadmaps = inProgressRoadmapRepository.findAllByMemberId(memberId);
         if (inProgressRoadmaps.isEmpty()) {
             throw new NotFoundException();
         }

         List<RoadmapDto> roadmapDtos= new ArrayList<>();
         inProgressRoadmaps.forEach( inProgressRoadmap -> {
             Roadmap roadmap = roadmapRepository.findById(inProgressRoadmap.getRoadmap().getId()).orElse(null);
             RoadmapDto roadmapdto = RoadmapDto.of(roadmap, roadmap.getMember());
             if (roadmapdto == null) {
             } else {
                 roadmapDtos.add(roadmapdto);
             }
         });

        return roadmapDtos;
    }
    public List<RoadmapDto> findRoadmapCreatedByMemberId(Long memberId){
        Optional<Member> member = memberRepository.findById(memberId);
        List<Roadmap> roadmaps = roadmapRepository.findByMemberId(memberId);
        return roadmaps.stream().map(roadmap -> RoadmapDto.of(roadmap, member.get())).toList();
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
    public boolean changeNodeStatus(NodeStatusChangeDto nodeStatusChangeDto) {

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

    public List<RoadmapDto> findRoadmapByKeyword(String keyword, Integer size, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return roadmapRepository.findyBySearchOption(pageRequest, keyword);
    }

}

package com.roadmaker.roadmap.service;

import com.roadmaker.commons.exception.ConflictException;
import com.roadmaker.commons.exception.NotFoundException;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.member.service.MemberService;
import com.roadmaker.roadmap.dto.*;
import com.roadmaker.roadmap.entity.comment.Comment;
import com.roadmaker.roadmap.entity.comment.CommentRepository;
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

    private final RoadmapRepository roadmapRepository;
    private final RoadmapNodeRepository roadmapNodeRepository;
    private final RoadmapEdgeRepository roadmapEdgeRepository;
    private final RoadmapEditorRepository roadmapEditorRepository;
    private final RoadmapViewportRepository roadmapViewportRepository;
    private final InProgressRoadmapRepository inProgressRoadmapRepository;
    private final InProgressNodeRepository inProgressNodeRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

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

    public List<RoadmapDto> findRoadmapJoinedByMemberId(Long memberId){

         List<InProgressRoadmap> inProgressRoadmaps = inProgressRoadmapRepository.findAllByMemberId(memberId);
         if (inProgressRoadmaps.isEmpty()) {
             throw new NotFoundException();
         }

         List<RoadmapDto> roadmapDtos= new ArrayList<>();
         inProgressRoadmaps.forEach( inProgressRoadmap -> {
             RoadmapDto roadmapdto = RoadmapDto.of(roadmapRepository.findById(inProgressRoadmap.getRoadmap().getId()).orElse(null));
             if (roadmapdto == null) {
             } else {
                 roadmapDtos.add(roadmapdto);
             }
         });

        return roadmapDtos;
    }
    public List<RoadmapDto> findRoadmapCreatedByMemberId(Long memberId){

        List<RoadmapEditor> roadmapsCreated = roadmapEditorRepository.findAllByMemberIdAndIsOwner(memberId, true);

        if(roadmapsCreated.isEmpty()) {
            throw new NotFoundException();
        }

        List<RoadmapDto> roadmapDtos= new ArrayList<>();
        roadmapsCreated.forEach( roadmapCreated -> {
            RoadmapDto roadmapdto = RoadmapDto.of(roadmapRepository.findById(roadmapCreated.getRoadmap().getId()).orElse(null));
            if (roadmapdto == null) {
            } else {
                roadmapDtos.add(roadmapdto);
            }
        });

        return roadmapDtos;
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

    public RoadmapResponse makeRoadmapResponse(RoadmapDto roadmapDto) {
        Long roadmapId = roadmapDto.getId();
        Optional<Roadmap> roadmapOptional = roadmapRepository.findById(roadmapId);
        Roadmap roadmap = roadmapOptional.orElse(null);
        if(roadmap == null) {
            return null;
        }
        return RoadmapResponse.of(roadmap);
    }

    public List<CommentDto> callRoadmapComment (Long roadmapId) {
        List<Comment> comments = commentRepository.findByRoadmapId(roadmapId);
        List<CommentDto> commentDtos = new ArrayList<>();
        comments.forEach(
                comment -> { CommentDto commentDto = CommentDto.builder()
                                .roadmapId(comment.getRoadmap().getId())
                        .memberNickname(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .build();
                    commentDtos.add(commentDto);
                }
        );
        return commentDtos;
    }

    public boolean saveComment (CommentDto commentDto, Long roadmapId) {
        Comment comment = Comment.builder()
                .roadmap(roadmapRepository.findById(roadmapId).orElse(null))
                .content(commentDto.getContent())
                .member(memberRepository.findByNickname(commentDto.getMemberNickname()).orElse(null))
                .build();

        if(comment.getRoadmap() == null || comment.getMember() == null) {
            return false;
        }

        commentRepository.save(comment);
        return true;
    }
}

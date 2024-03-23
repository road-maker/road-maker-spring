package com.roadmaker.roadmap.service;

import com.roadmaker.comment.entity.CommentRepository;
import com.roadmaker.global.error.exception.NotFoundException;
import com.roadmaker.image.dto.UploadImageResponse;
import com.roadmaker.image.service.ImageService;
import com.roadmaker.inprogressroadmap.entity.InProgressRoadmap;
import com.roadmaker.inprogressroadmap.entity.InProgressRoadmapRepository;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.roadmap.dto.*;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmap.RoadmapRepository;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdge;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdgeRepository;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNodeRepository;
import com.roadmaker.roadmap.entity.roadmapviewport.RoadmapViewport;
import com.roadmaker.roadmap.entity.roadmapviewport.RoadmapViewportRepository;
import com.roadmaker.roadmap.exception.RoadmapAlreadyJoinedException;
import com.roadmaker.roadmap.exception.RoadmapForbiddenAccessException;
import com.roadmaker.roadmap.exception.RoadmapNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final RoadmapNodeRepository roadmapNodeRepository;
    private final RoadmapEdgeRepository roadmapEdgeRepository;
    private final RoadmapViewportRepository roadmapViewportRepository;
    private final InProgressRoadmapRepository inProgressRoadmapRepository;
    private final InProgressNodeRepository inProgressNodeRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${ip-address}")
    private String ipAddress;

    private static final String SORT_PROPERTIES = "CreatedAt";

    @Transactional
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

    @Transactional
    public UploadImageResponse uploadThumbnail(Long roadmapId, Long memberId, String thumbnailUrl) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(RoadmapNotFoundException::new);

        if (!roadmap.isOwner(memberId)) {
            throw new RoadmapForbiddenAccessException("자신이 생성한 로드에만 썸네일을 등록할 수 있습니다.");
        }

        roadmap.updateThumbnail(thumbnailUrl);

        return UploadImageResponse.builder()
                .url(thumbnailUrl)
                .build();
    }

    public RoadmapFindResponse findByPage(Integer page, Integer size, String flag) {

        Page<Roadmap> roadmaps = null;
        PageRequest pageRequest = null;
        if (Objects.equals(flag, "recent")) { //default: 최신순
            pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, SORT_PROPERTIES));
            roadmaps = roadmapRepository.findAll(pageRequest);

            List<RoadmapDto> roadmapsDtoList = roadmaps.stream().map(roadmap -> RoadmapDto.of(roadmap, roadmap.getMember())).toList();

            String next = ipAddress + "api/roadmaps?page=" + (pageRequest.getPageNumber() + 2) + "&order-type=" + flag;
            String previous = ipAddress + "api/roadmaps?page=" + (pageRequest.getPageNumber()) + "&order-type=" + flag;
            if (pageRequest.getPageNumber() == 0) {
                previous = null;
            } else if (pageRequest.getPageNumber() == roadmaps.getTotalPages() - 1) {
                next = null;
            }

            return RoadmapFindResponse.builder()
                    .totalPage((long) roadmaps.getTotalPages())
                    .next(next)
                    .previous(previous)
                    .result(roadmapsDtoList)
                    .build();

        } else if (Objects.equals(flag, "most-liked")) { // 좋아요 순, querydsl을 써보자
            pageRequest = PageRequest.of(page - 1, size);
            return roadmapRepository.orderByLikes(pageRequest);
        } else {
            throw new NotFoundException("orderType이 정의되지 않음");
        } //다른 검색 옵션(추가 가능)
    }

    public Roadmap findRoadmapById(Long roadmapId) {
        return roadmapRepository.findById(roadmapId).orElseThrow(RoadmapNotFoundException::new);
    }

    public List<RoadmapDto> findRoadmapJoinedByMemberId(Long memberId) {

        List<InProgressRoadmap> inProgressRoadmaps = inProgressRoadmapRepository.findAllByMemberId(memberId, Sort.by(Sort.Direction.DESC, SORT_PROPERTIES));
        List<RoadmapDto> roadmapDtos = new ArrayList<>();

        if (inProgressRoadmaps.isEmpty()) {
            RoadmapDto roadmapdto = RoadmapDto.builder()
                    .build();
            roadmapDtos.add(roadmapdto);
            return roadmapDtos; // 진행중인 로드맵이 없는 경우
        }

        inProgressRoadmaps.forEach(inProgressRoadmap -> {
            Roadmap roadmap = roadmapRepository.findById(inProgressRoadmap.getRoadmap().getId()).orElse(null);
            RoadmapDto roadmapdto = RoadmapDto.of(Objects.requireNonNull(roadmap), roadmap.getMember());
            if (roadmapdto != null) {
                roadmapDtos.add(roadmapdto);
            }
        });

        return roadmapDtos;
    }

    public List<RoadmapDto> findRoadmapCreatedByMemberId(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        List<Roadmap> roadmaps = roadmapRepository.findByMemberId(memberId, Sort.by(Sort.Direction.DESC, SORT_PROPERTIES));
        return roadmaps.stream().map(roadmap -> RoadmapDto.of(roadmap, member.get())).toList();
    }


    @Transactional
    public Integer joinRoadmap(Roadmap roadmap, Member member) {
        // 이미 참여중인지 확인
        Optional<InProgressRoadmap> inProgressRoadmapOptional = inProgressRoadmapRepository.findByRoadmapIdAndMemberId(roadmap.getId(), member.getId());
        if (inProgressRoadmapOptional.isPresent()) {
            throw new RoadmapAlreadyJoinedException();
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
                .forEach(node -> {
                    InProgressNode inProgressNode = InProgressNode.builder()
                            .roadmap(roadmap)
                            .roadmapNode(node)
                            .member(member)
                            .inProgressRoadmap(inProgressRoadmap)
                            .done(false)
                            .build();
                    inProgressNodeRepository.save(inProgressNode);
                });

        return inProgressRoadmapRepository.countByRoadmapId(roadmap.getId());
    }

    @Transactional
    public boolean changeNodeStatus(NodeStatusChangeDto nodeStatusChangeDto) {

        Long nodeId = nodeStatusChangeDto.getInProgressNodeId();
        Optional<InProgressNode> inProgressNodeOptional = inProgressNodeRepository.findById(nodeId);
        InProgressNode inProgressNode = inProgressNodeOptional.orElse(null);
        if (inProgressNode == null) {
            return false;
        }

        inProgressNode.setDone(!Boolean.TRUE.equals(nodeStatusChangeDto.getDone()));

        return true;
    }

    public RoadmapFindResponse findRoadmapByKeyword(String keyword, Integer size, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, SORT_PROPERTIES));
        return roadmapRepository.findBySearchOption(pageRequest, keyword);
    }
}

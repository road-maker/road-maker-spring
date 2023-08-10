package com.roadmaker.roadmap.service;

import com.roadmaker.comment.entity.CommentRepository;
import com.roadmaker.commons.exception.ConflictException;
import com.roadmaker.commons.exception.NotFoundException;
import com.roadmaker.image.dto.UploadImageResponse;
import com.roadmaker.image.service.ImageService;
import com.roadmaker.inprogressroadmap.entity.InProgressRoadmap;
import com.roadmaker.inprogressroadmap.entity.InProgressRoadmapRepository;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.roadmap.dto.*;
import com.roadmaker.roadmap.entity.blogkeyword.BlogKeyword;
import com.roadmaker.roadmap.entity.blogkeyword.BlogKeywordRepository;
import com.roadmaker.roadmap.entity.bojprob.BojProb;
import com.roadmaker.roadmap.entity.bojprob.BojProbRepository;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.roadmap.entity.inprogressnode.QInProgressNode;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmap.RoadmapRepository;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdge;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdgeRepository;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNodeRepository;
import com.roadmaker.roadmap.entity.roadmapviewport.RoadmapViewport;
import com.roadmaker.roadmap.entity.roadmapviewport.RoadmapViewportRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final ImageService imageService;
    private final BlogKeywordRepository blogKeywordRepository;
    private final BojProbRepository bojProbRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    private enum OrderType {
//        RECENT, MOSTLIKED
//    }

    @Value("${ip-address}")
    private String ipAddress;

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
    @Transactional
    public UploadImageResponse uploadThumbnail(Roadmap roadmap, MultipartFile image) throws IOException {
        String imageUrl = imageService.uploadImage(image);
        roadmap.setThumbnailUrl(imageUrl);

        return UploadImageResponse.builder().url(imageUrl).build();
    }

    @Override
    public RoadmapFindResponse findByPage(Integer page, Integer size, String flag) {

        Page<Roadmap> roadmaps = null;
        PageRequest pageRequest = null;
        if (Objects.equals(flag, "recent")) { //default: 최신순
            pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            roadmaps = roadmapRepository.findAll(pageRequest);

            List<RoadmapDto> roadmapsDtoList = roadmaps.stream().map(roadmap -> RoadmapDto.of(roadmap, roadmap.getMember())).toList();

            String next = ipAddress + "api/roadmaps?page=" + (pageRequest.getPageNumber() + 2)+"&order-type="+flag;
            String previous = ipAddress + "api/roadmaps?page=" + (pageRequest.getPageNumber())+"&order-type="+flag;
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
        } else {throw new NotFoundException("orderType이 정의되지 않음");} //다른 검색 옵션(추가 가능)
    }

    @Override
    public Roadmap findRoadmapById(Long roadmapId) {
        return roadmapRepository.findById(roadmapId).orElseThrow(NotFoundException::new);
    }

    public List<RoadmapDto> findRoadmapJoinedByMemberId(Long memberId){

        List<InProgressRoadmap> inProgressRoadmaps = inProgressRoadmapRepository.findAllByMemberId(memberId, Sort.by(Sort.Direction.DESC, "CreatedAt"));
        List<RoadmapDto> roadmapDtos= new ArrayList<>();

        if (inProgressRoadmaps.isEmpty()) {
            RoadmapDto roadmapdto = RoadmapDto.builder()
                    .build();
            roadmapDtos.add(roadmapdto);
            return roadmapDtos; // 진행중인 로드맵이 없는 경우
         }

         inProgressRoadmaps.forEach( inProgressRoadmap -> {
             Roadmap roadmap = roadmapRepository.findById(inProgressRoadmap.getRoadmap().getId()).orElse(null);
             RoadmapDto roadmapdto = RoadmapDto.of(Objects.requireNonNull(roadmap), roadmap.getMember());
             if (roadmapdto != null) {
                 roadmapDtos.add(roadmapdto);
             }
         });

        return roadmapDtos;
    }
    public List<RoadmapDto> findRoadmapCreatedByMemberId(Long memberId){
        Optional<Member> member = memberRepository.findById(memberId);
        List<Roadmap> roadmaps = roadmapRepository.findByMemberId(memberId, Sort.by(Sort.Direction.DESC, "CreatedAt"));
        return roadmaps.stream().map(roadmap -> RoadmapDto.of(roadmap, member.get())).toList();
    }


    @Override
    @Transactional
    public Integer joinRoadmap(Roadmap roadmap, Member member) {
        // 이미 참여중인지 확인
        Optional<InProgressRoadmap> inProgressRoadmapOptional = inProgressRoadmapRepository.findByRoadmapIdAndMemberId(roadmap.getId(), member.getId());
        if (inProgressRoadmapOptional.isPresent()) {
            throw new ConflictException("Already joined this roadmap");
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

        return inProgressRoadmapRepository.countByRoadmapId(roadmap.getId());
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

    //얘도 수정 염두
    public RoadmapFindResponse findRoadmapByKeyword(String keyword, Integer size, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "CreatedAt"));
        return roadmapRepository.findBySearchOption(pageRequest, keyword);
    }

    @Override
    public Boolean setBlogKeyword(BlogKeywordRequest request) {
        Long roadmapNodeId = request.getRoadmapNodeId();
        String keyword = request.getKeyword();

        // keyword가 존재하면 false를 반환하며 종료
        BlogKeyword existingKeyword = blogKeywordRepository.findByRoadmapNodeId(roadmapNodeId);
        if (existingKeyword != null) {
            return false;
        }
        // 새로운 키워드 등록
        BlogKeyword blogKeyword = BlogKeyword.builder()
                .roadmapNodeId(roadmapNodeId)
                .keyword(keyword)
                .build();

        blogKeywordRepository.save(blogKeyword);

        return true;
    }

    @Override
    public Boolean setBojProblem(BojProbRequest request) {
        Long roadmapNodeId = request.getRoadmapNodeId();
        String bojNumber = request.getProbNumber();

        BojProb bojProblemExists = bojProbRepository.findByRoadmapNodeId(roadmapNodeId);
        if (bojProblemExists != null) {
            return false;
        }

        try {
            // Jsoup을 이용하여 해당 웹 페이지에 연결
            String url = String.format("https://www.acmicpc.net/problem/%s", bojNumber);
            Document doc = Jsoup.connect(url).get();

            String bojTitle = doc.getElementById("problem_title").text();

            BojProb bojProb = BojProb.builder()
                    .roadmapNodeId(roadmapNodeId)
                    .bojNumber(bojNumber)
                    .bojTitle(bojTitle)
                    .build();

            bojProbRepository.save(bojProb);
            return true;

        } catch (Exception e) {
            logger.debug("Error:", e);
            return false;
        }
    }
}

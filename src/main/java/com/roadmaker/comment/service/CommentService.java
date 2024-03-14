package com.roadmaker.comment.service;

import com.roadmaker.comment.dto.request.CommentCreateRequest;
import com.roadmaker.comment.dto.response.CommentResponse;
import com.roadmaker.comment.entity.Comment;
import com.roadmaker.comment.entity.CommentRepository;
import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmap.RoadmapRepository;
import com.roadmaker.roadmap.exception.RoadmapNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final RoadmapRepository roadmapRepository;

    // api 주소 전달
    @Value("${ip-address}")
    private String ipAddress;
    private static final String COMMENT_PAGE = "/comments?page=";

    @Transactional
    public void createComment(CommentCreateRequest request, Member member) {
        Roadmap roadmap = roadmapRepository.findById(request.getRoadmapId())
                .orElseThrow(RoadmapNotFoundException::new);

        Comment comment = Comment.create(request.getContent(), roadmap, member);

        commentRepository.save(comment);
    }

    public CommentResponse findCommentByRoadmapIdAndPageRequest(Long roadmapId, Integer page, Integer size) {
        // pageable을 통해 comment를 찾아 commentDTO로 변환

        boolean isExist = roadmapRepository.existsById(roadmapId);
        if (!isExist) {
            throw new RoadmapNotFoundException();
        }

        // 1 페이지부터 시작하도록
        int pageMod = page - 1;

        //페이지네이션, commentDto로 가공, 불러온 페이지 내에 넘버링 부여
        PageRequest pageRequest = PageRequest.of(pageMod, size, Sort.by(Sort.Direction.DESC, "CreatedAt"));
        Page<CommentCreateRequest> comments = commentRepository.findCommentByRoadmapId(roadmapId, pageRequest).map(CommentCreateRequest::of);


        // 페이지 주소 설정
        String next = ipAddress + "api/roadmaps/load-roadmap/" + roadmapId + COMMENT_PAGE + (pageRequest.getPageNumber() + 2);
        String previous = ipAddress + "api/roadmaps/load-roadmap/" + roadmapId + COMMENT_PAGE + (pageRequest.getPageNumber());
        if (pageRequest.getPageNumber() == 0) {
            previous = null;
        } else if (pageRequest.getPageNumber() == comments.getTotalPages() - 1) {
            next = null;
        }

        return CommentResponse.builder()
                .totalPage((long) comments.getTotalPages())
                .previous(previous)
                .next(next)
                .result(comments.getContent())
                .build();
    }

    public CommentResponse findByMemberIdAndPageRequest(Long memberId, Integer page, Integer size) {

        // 1 페이지부터 시작하도록
        int pageMod = page - 1;

        //페이지네이션, commentDto로 가공, 불러온 페이지 내에 넘버링 부여
        PageRequest pageRequest = PageRequest.of(pageMod, size, Sort.by(Sort.Direction.DESC, "CreatedAt"));
        Page<CommentCreateRequest> comments = commentRepository.findCommentByMemberId(memberId, pageRequest).map(CommentCreateRequest::of);

        // 페이지 주소 설정
        String next = ipAddress + "api/members/" + memberId + COMMENT_PAGE + (pageRequest.getPageNumber() + 2);
        String previous = ipAddress + "api/members/" + memberId + COMMENT_PAGE + (pageRequest.getPageNumber());
        if (pageRequest.getPageNumber() == 0) {
            previous = null;
        } else if (pageRequest.getPageNumber() == comments.getTotalPages() - 1) {
            next = null;
        }

        return CommentResponse.builder()
                .totalPage((long) comments.getTotalPages())
                .previous(previous)
                .next(next)
                .result(comments.getContent())
                .build();
    }
}

package com.roadmaker.comment.service;

import com.roadmaker.comment.dto.CommentDto;
import com.roadmaker.comment.dto.CommentResponse;
import com.roadmaker.comment.entity.Comment;
import com.roadmaker.comment.entity.CommentRepository;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.roadmap.dto.RoadmapResponse;
import com.roadmaker.roadmap.entity.roadmap.RoadmapRepository;
import com.roadmaker.roadmap.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;

    public CommentResponse findCommentByRoadmapIdAndPageRequest (Long roadmapId, Integer page, Integer size) {
        // pageable을 통해 comment를 찾아 commentDTO로 변환
        int pageMod = page-1;
        PageRequest pageRequest = PageRequest.of(pageMod, size);
        Page<CommentDto> comments = commentRepository.findCommentByRoadmapId(roadmapId, pageRequest).map(CommentDto::of);
        AtomicInteger counter = new AtomicInteger(1);
        comments.forEach(
                commentDto -> {
                    commentDto.setNumbering((long) ((pageMod)*size + counter.getAndIncrement()));
                });

        String next = null;
        String previous = null;

        if(pageRequest.getPageNumber() == 0) {
            next = "http://52.79.185.147/api/roadmaps/load-roadmap/"+roadmapId+"/comments?page=" + (pageRequest.getPageNumber()+2) + "&size="+pageRequest.getPageSize();
        } else if (pageRequest.getPageNumber() == comments.getTotalPages() - 1) {
            previous = "http://52.79.185.147/api/roadmapsload-roadmap/"+roadmapId+"/comments?page=" + (pageRequest.getPageNumber()) + "&size="+pageRequest.getPageSize();
        } else {
            next = "http://52.79.185.147/api/roadmapsload-roadmap/"+roadmapId+"/comments?page=" + (pageRequest.getPageNumber()+2) + "&size="+pageRequest.getPageSize();
            previous = "http://52.79.185.147/api/roadmapsload-roadmap/"+roadmapId+"/comments?page=" + (pageRequest.getPageNumber()) + "&size="+pageRequest.getPageSize();
        }

        return CommentResponse.builder()
                .totalPage((long)comments.getTotalPages())
                .previous(previous)
                .next(next)
                .result(comments.getContent())
                .build();
    }

    @Override
    public CommentResponse findByMemberIdAndPageRequest(Long memberId, Integer page, Integer size) {
        int pageMod = page-1;
        PageRequest pageRequest = PageRequest.of(pageMod, size);
        Page<CommentDto> comments = commentRepository.findCommentByMemberId(memberId, pageRequest).map(CommentDto::of);
        AtomicInteger counter = new AtomicInteger(1);
        comments.forEach(
                commentDto -> {
                    commentDto.setNumbering((long) ((pageMod)*size + counter.getAndIncrement()));
                });

        String next = null;
        String previous = null;

        if(pageRequest.getPageNumber() == 0) {
            next = "http://52.79.185.147/api/members/"+memberId+"/comments?page=" + (pageRequest.getPageNumber()+2) + "&size="+pageRequest.getPageSize();
        } else if (pageRequest.getPageNumber() == comments.getTotalPages() - 1) {
            previous = "http://52.79.185.147/api/members/"+memberId+"/comments?page=" + (pageRequest.getPageNumber()) + "&size="+pageRequest.getPageSize();
        } else {
            next = "http://52.79.185.147/api/members/"+memberId+"/comments?page=" + (pageRequest.getPageNumber()+2) + "&size="+pageRequest.getPageSize();
            previous = "http://52.79.185.147/api/members/"+memberId+"/comments?page=" + (pageRequest.getPageNumber()) + "&size="+pageRequest.getPageSize();
        }

        return CommentResponse.builder()
                .totalPage((long)comments.getTotalPages())
                .previous(previous)
                .next(next)
                .result(comments.getContent())
                .build();
    }

    public boolean saveComment (CommentDto commentDto, Member member) {
        Comment comment = Comment.builder()
                .roadmap(roadmapRepository.findRoadmapById(commentDto.getRoadmapId()))
                .content(commentDto.getContent())
                .member(member)
                .build();

        if(comment.getRoadmap() == null || comment.getMember() == null) {
            return false;
        }

        commentRepository.save(comment);
        return true;
    }
}

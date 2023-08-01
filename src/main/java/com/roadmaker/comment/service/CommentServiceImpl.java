package com.roadmaker.comment.service;

import com.roadmaker.comment.dto.CommentDto;
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

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;

    public List<CommentDto> findCommentByRoadmapIdAndPageRequest (Long roadmapId, Integer page, Integer size) {
        // pageable을 통해 comment를 찾아 commentDTO로 변환
        PageRequest pageRequest = PageRequest.of(page, size);
        return commentRepository.findCommentByRoadmapId(roadmapId, pageRequest).map(CommentDto::of).getContent();
    }

    @Override
    public List<CommentDto> findByMemberIdAndPageRequest(Long memberId, Integer page, Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        return commentRepository.findCommentByMemberId(memberId, pageRequest).map(CommentDto::of).getContent();
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

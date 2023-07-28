package com.roadmaker.comment.service;

import com.roadmaker.comment.dto.CommentDto;
import com.roadmaker.comment.entity.Comment;
import com.roadmaker.comment.entity.CommentRepository;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.roadmap.dto.RoadmapResponse;
import com.roadmaker.roadmap.entity.roadmap.RoadmapRepository;
import com.roadmaker.roadmap.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;

    public List<CommentDto> callRoadmapComment (Long roadmapId) {
        List<Comment> comments = commentRepository.findAllByRoadmapId(roadmapId);
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

    public List<CommentDto> callMemberComment (Long memberId) {
        List<Comment> comments = commentRepository.findAllByMemberId(memberId);
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

    public boolean saveComment (CommentDto commentDto) {
        Comment comment = Comment.builder()
                .roadmap(roadmapRepository.findRoadmapById(commentDto.getRoadmapId()))
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

package com.roadmaker.comment.service;

import com.roadmaker.comment.dto.CommentDto;
import com.roadmaker.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CommentService {
    public List<CommentDto> findCommentByRoadmapIdAndPageRequest (Long roadmapId, Integer page, Integer size);
    public List<CommentDto> findByMemberIdAndPageRequest (Long memberId, Integer page, Integer size);
    public boolean saveComment (CommentDto commentDto, Member member);
    }

package com.roadmaker.comment.service;

import com.roadmaker.comment.dto.CommentDto;
import com.roadmaker.comment.dto.CommentResponse;
import com.roadmaker.member.entity.Member;

public interface CommentService {
    public CommentResponse findCommentByRoadmapIdAndPageRequest (Long roadmapId, Integer page, Integer size);
    public CommentResponse findByMemberIdAndPageRequest (Long memberId, Integer page, Integer size);
    public boolean saveComment (CommentDto commentDto, Member member);
    }

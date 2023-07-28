package com.roadmaker.comment.service;

import com.roadmaker.comment.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CommentService {
    public List<CommentDto> callRoadmapComment (Long roadmapId);
    public boolean saveComment (CommentDto commentDto);
    public List<CommentDto> callMemberComment (Long memberId);

    }

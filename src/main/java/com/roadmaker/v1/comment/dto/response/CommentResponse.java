package com.roadmaker.v1.comment.dto.response;

import com.roadmaker.v1.comment.dto.request.CommentCreateRequest;
import lombok.*;

import java.util.List;

@Getter
@ToString
public class CommentResponse {
    private final Long totalPage;
    private final String next;
    private final String previous;
    private final List<CommentCreateRequest> result;

    @Builder
    private CommentResponse(Long totalPage, String next, String previous, List<CommentCreateRequest> result) {
        this.totalPage = totalPage;
        this.next = next;
        this.previous = previous;
        this.result = result;
    }
}

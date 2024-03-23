package com.roadmaker.v1.comment.dto.request;

import com.roadmaker.v1.comment.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class CommentCreateRequest {
    @NotNull
    Long roadmapId;

    @NotBlank
    String content;

    @Builder
    private CommentCreateRequest(Long roadmapId, String content) {
        this.roadmapId = roadmapId;
        this.content = content;
    }

    public static CommentCreateRequest of(Comment comment) {
        return CommentCreateRequest.builder()
                .content(comment.getContent())
                .roadmapId(comment.getRoadmap().getId())
                .build();
    }
}

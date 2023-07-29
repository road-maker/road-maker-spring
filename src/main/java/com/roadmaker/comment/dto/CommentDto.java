package com.roadmaker.comment.dto;

import com.roadmaker.comment.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor
public class CommentDto {
    @NotBlank
    Long roadmapId;
    @NotBlank
    String memberNickname;
    @NotBlank
    String content;

    LocalDateTime createdTime;
    LocalDateTime updatedTime;

    public static CommentDto of(Comment comment) {
        return CommentDto.builder()
                .createdTime(comment.getCreatedAt())
                .updatedTime(comment.getUpdatedAt())
                .content(comment.getContent())
                .memberNickname(comment.getMember().getNickname())
                .roadmapId(comment.getRoadmap().getId())
                .build();
    }
}

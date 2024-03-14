package com.roadmaker.comment.dto.request;

import com.roadmaker.comment.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data @Builder @AllArgsConstructor
public class CommentCreateRequest {
    @NotBlank
    Long roadmapId;
    @NotBlank
    String content;
    @NotBlank
    String nickname;

    String createdAt;
    String updatedAt;

    private static String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. dd");
        return dateTime.format(formatter);
    }

    public static CommentCreateRequest of(Comment comment) {
        return CommentCreateRequest.builder()
                .createdAt(formatDate(comment.getCreatedAt()))
                .updatedAt(formatDate(comment.getUpdatedAt()))
                .nickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .roadmapId(comment.getRoadmap().getId())
                .build();
    }
}

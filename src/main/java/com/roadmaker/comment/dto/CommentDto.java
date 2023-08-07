package com.roadmaker.comment.dto;

import com.roadmaker.comment.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data @Builder @AllArgsConstructor
public class CommentDto {
    @NotBlank
    Long roadmapId;
    @NotBlank
    String content;
    @NotBlank
    Long numbering;
    @NotBlank
    String memberNickname;

    String createdAt;
    String updatedAt;

    private static String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. dd.");
        return dateTime.format(formatter);
    }

    public static CommentDto of(Comment comment) {
        return CommentDto.builder()
                .createdAt(formatDate(comment.getCreatedAt()))
                .updatedAt(formatDate(comment.getUpdatedAt()))
                .memberNickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .roadmapId(comment.getRoadmap().getId())
                .build();
    }
}

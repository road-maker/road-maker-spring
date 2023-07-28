package com.roadmaker.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @Builder @AllArgsConstructor
public class CommentDto {
    @NotBlank
    Long roadmapId;
    @NotBlank
    String memberNickname;
    @NotBlank
    String content;
}

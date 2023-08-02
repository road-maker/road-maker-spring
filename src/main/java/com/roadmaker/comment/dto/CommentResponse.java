package com.roadmaker.comment.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CommentResponse {
    Long totalPage;
    String next;
    String previous;
    List<CommentDto> result;
}

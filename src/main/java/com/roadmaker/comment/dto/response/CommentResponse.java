package com.roadmaker.comment.dto.response;

import com.roadmaker.comment.dto.request.CommentCreateRequest;
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
    List<CommentCreateRequest> result;
}

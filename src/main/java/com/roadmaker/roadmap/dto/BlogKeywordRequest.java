package com.roadmaker.roadmap.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BlogKeywordRequest {
    private Long roadmapNodeId;
    private String keyword;
}

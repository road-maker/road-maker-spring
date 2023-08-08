package com.roadmaker.roadmap.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BojProbRequest {
    private Long roadmapNodeId;
    private String probNumber;
}

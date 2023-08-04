package com.roadmaker.roadmap.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RoadmapFindResponse {
    Long totalPage;
    String next;
    String previous;
    List<RoadmapDto> result;
}

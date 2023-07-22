package com.roadmaker.roadmap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class InProgressNodeDto {
    private String id;
    private String roadmapId;
    private String roadmapNodeId;
    private String memberId;
    private Boolean done;
}

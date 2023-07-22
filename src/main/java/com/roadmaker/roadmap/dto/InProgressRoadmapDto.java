package com.roadmaker.roadmap.dto;

import com.roadmaker.member.domain.entity.Member;
import com.roadmaker.roadmap.domain.entity.Roadmap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class InProgressRoadmapDto {
    private String id;
    private String roadmapId;
    private String memberId;
    private Boolean done;
}

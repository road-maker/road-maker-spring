package com.roadmaker.member.dto;

import com.roadmaker.roadmap.dto.InProgressRoadmapDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MypageResponse {
    private Long memberId;
    private String email;
    private String nickname;
    private String bio;
    private String blogUrl;
    private String backjoonId;
    private List<InProgressRoadmapDto> inProcessRoadmaps;
}

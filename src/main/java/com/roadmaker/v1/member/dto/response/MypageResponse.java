package com.roadmaker.v1.member.dto.response;

import com.roadmaker.v1.roadmap.dto.InProgressRoadmapDto;
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

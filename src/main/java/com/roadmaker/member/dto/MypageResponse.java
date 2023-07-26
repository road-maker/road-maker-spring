package com.roadmaker.member.dto;

import com.roadmaker.roadmap.dto.InProgressRoadmapDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    private String avatarUrl;
    private String githubUrl;
    private String blogUrl;
    private String backjoonId;
    private int level;
    private int exp;
    private List<InProgressRoadmapDto> inProcessRoadmapDto;
}

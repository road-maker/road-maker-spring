package com.roadmaker.member.dto;

import com.roadmaker.comment.dto.CommentDto;
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
//    private String avatarUrl;
//    private String githubUrl;
    private String blogUrl;
    private String backjoonId;
//    private int level;
//    private int exp;
    private List<InProgressRoadmapDto> inProcessRoadmaps;
}

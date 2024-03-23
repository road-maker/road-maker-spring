package com.roadmaker.v1.roadmap.exception;

import com.roadmaker.global.error.ApiException;
import org.springframework.http.HttpStatus;

public class RoadmapAlreadyJoinedException extends ApiException {
    public RoadmapAlreadyJoinedException() {
        super(HttpStatus.CONFLICT, "이미 참여중인 로드맵입니다.");
    }
}

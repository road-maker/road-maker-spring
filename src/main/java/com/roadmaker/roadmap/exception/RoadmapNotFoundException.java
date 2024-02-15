package com.roadmaker.roadmap.exception;

import com.roadmaker.global.error.ApiException;
import org.springframework.http.HttpStatus;

public class RoadmapNotFoundException extends ApiException {
    public RoadmapNotFoundException() {
        super(HttpStatus.NOT_FOUND, "해당 로드맵을 찾을 수 없습니다.");
    }
}

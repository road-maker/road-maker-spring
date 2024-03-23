package com.roadmaker.roadmap.exception;

import com.roadmaker.global.error.ApiException;
import org.springframework.http.HttpStatus;

public class RoadmapForbiddenAccessException extends ApiException {
    public RoadmapForbiddenAccessException() {
        super(HttpStatus.FORBIDDEN, "로드맵에 대한 접근 권한이 없습니다.");
    }

    public RoadmapForbiddenAccessException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}

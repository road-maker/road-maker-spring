package com.roadmaker.v1.auth.dto.response;

import lombok.Builder;

@Builder
public record AuthSignupResponse(Long id, String accessToken) {
    public static AuthSignupResponse of(Long id, String accessToken) {
        return AuthSignupResponse.builder()
                .id(id)
                .accessToken(accessToken)
                .build();
    }
}

package com.roadmaker.v1.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthLoginRequest(@NotBlank String email, @NotBlank String password) {
}

package com.roadmaker.v1.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberLoginRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}

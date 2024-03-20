package com.roadmaker.member.dto.response;

import com.roadmaker.member.entity.Member;
import lombok.*;

@Builder
public record MemberLoginResponse(MemberResponse member, String accessToken) {
    public static MemberLoginResponse of(Member member, String accessToken) {
        return MemberLoginResponse.builder()
                .member(MemberResponse.of(member))
                .accessToken(accessToken)
                .build();
    }
}

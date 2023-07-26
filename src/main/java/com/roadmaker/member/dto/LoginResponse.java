package com.roadmaker.member.dto;

import com.roadmaker.member.entity.Member;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LoginResponse {
    private TokenInfo tokenInfo;
    private MemberResponse member;

    public static LoginResponse of(Member member, TokenInfo tokenInfo) {
        return LoginResponse.builder()
                .member(MemberResponse.of(member))
                .tokenInfo(tokenInfo)
                .build();
    }
}

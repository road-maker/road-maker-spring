package com.roadmaker.v1.member.dto.response;

import com.roadmaker.v1.member.entity.Member;
import lombok.*;

@Getter
@Builder
public class MemberLoginResponse {
    private final MemberResponse member;
    private final TokenInfo tokenInfo;

    @Builder
    private MemberLoginResponse(MemberResponse member, TokenInfo tokenInfo) {
        this.member = member;
        this.tokenInfo = tokenInfo;
    }

    public static MemberLoginResponse of(Member member, String accessToken) {
        return MemberLoginResponse.builder()
                .member(MemberResponse.of(member))
                .tokenInfo(TokenInfo.of(accessToken))
                .build();
    }

    @Builder
    private record TokenInfo(String accessToken) {
        public static TokenInfo of(String accessToken) {
                return TokenInfo.builder()
                        .accessToken(accessToken)
                        .build();
            }
        }
}

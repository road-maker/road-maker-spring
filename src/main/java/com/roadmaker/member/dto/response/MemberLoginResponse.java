package com.roadmaker.member.dto.response;

import com.roadmaker.member.entity.Member;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberLoginResponse {
    private TokenInfo tokenInfo;
    private MemberResponse member;

    public static MemberLoginResponse of(Member member, TokenInfo tokenInfo) {
        return MemberLoginResponse.builder()
                .member(MemberResponse.of(member))
                .tokenInfo(tokenInfo)
                .build();
    }
}

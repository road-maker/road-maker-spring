package com.roadmaker.v1.member.service;

import com.roadmaker.v1.member.dto.response.MemberFindResponse;
import com.roadmaker.v1.member.dto.response.MemberResponse;
import com.roadmaker.v1.member.dto.request.MemberUpdateRequest;
import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.member.entity.MemberRepository;
import com.roadmaker.v1.member.exception.MemberNotFoundException;
import com.roadmaker.v1.member.exception.NicknameAlreadyRegisteredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberAvatarUpdateResponse updateAvatarUrl(Member member, String avatarUrl) {
        member.updateAvatarUrl(avatarUrl);
        return MemberAvatarUpdateResponse.builder().avatarUrl(avatarUrl).build();
    }

    @Transactional
    public void updateProfile(MemberUpdateRequest request, Member member) {
        validateNicknameUpdate(member.getNickname(), request.nickname());

        member.updateProfile(request.nickname(), request.bio(), request.blogUrl(), request.githubUrl());
    }

    public MemberResponse findMemberByNickname(String nickname) {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    public MemberFindResponse findMemberByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        return MemberFindResponse.of(member);
    }

    private void validateNicknameUpdate(String oldNickname, String newNickname) {
        if (!oldNickname.equals(newNickname)) {
            checkNicknameDuplicate(newNickname);
        }
    }

    private void checkNicknameDuplicate(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new NicknameAlreadyRegisteredException();
        }
    }
}

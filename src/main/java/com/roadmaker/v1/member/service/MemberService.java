package com.roadmaker.v1.member.service;

import com.roadmaker.v1.image.dto.UploadImageResponse;
import com.roadmaker.v1.image.service.ImageService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    @Transactional
    public UploadImageResponse uploadMemberAvatar(Member member, MultipartFile image) throws IOException {
        String imageUrl = imageService.uploadImage(image);
        member.setAvatarUrl(imageUrl);

        return UploadImageResponse.builder().url(imageUrl).build();
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

    public MemberResponse findMemberByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
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

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
    public MemberResponse saveProfile(MemberUpdateRequest request, Member member) {
        //1. 내가 입력한 닉네임이 이미 내 닉네임과 동일한 경우 충돌 피하기 위함
        if (!request.getNickname().equals(member.getNickname())) {
            //2. 다른 동일한 닉네임이 존재할 경우 409리턴하도록
            if (isDuplicatedNickname(request.getNickname())) {
                throw new NicknameAlreadyRegisteredException();
            }
            member.setNickname(request.getNickname());
        }

        member.setBio(request.getBio());
        member.setBlogUrl(request.getBlogUrl());

        return MemberResponse.of(member);
    }

    public MemberResponse findMemberByNickname(String nickname) {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse findMemberByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    private boolean isDuplicatedNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }
}

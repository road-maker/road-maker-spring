package com.roadmaker.member.service;

import com.roadmaker.member.authentication.JwtProvider;
import com.roadmaker.member.dto.MypageRequest;
import com.roadmaker.member.dto.MypageResponse;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.member.dto.TokenInfo;
import com.roadmaker.roadmap.dto.InProgressRoadmapDto;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.roadmap.entity.inprogressroadmap.InProgressRoadmap;
import com.roadmaker.roadmap.entity.inprogressroadmap.InProgressRoadmapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.roadmaker.member.authentication.SecurityUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service @Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;
    private final InProgressNodeRepository inProgressNodeRepository;
    private final InProgressRoadmapRepository inProgressRoadmapRepository;

    @Override
    @Transactional
    public void signUp(Member member) {
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public TokenInfo login(String email, String password) {
        // 로그인 ID/PW를 기반으로 authentication객체 생성
        // authentication은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(email, password);
        //검증: 비밀번호 체크. authenticate 메서드가 실행될 때,
        //CustomUserDetailsService에서 만든 loadByUsername실행
        Authentication authentication
                = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //인증 정보 기반으로 JWT 토큰 생성
        return jwtProvider.generateToken(authentication);
    }

    @Override
    public boolean isUserRegistered(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.isPresent();
    }

    public Member getLoggedInMember() {
        String email = SecurityUtil.getLoggedInMemberEmail();
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        System.out.println(email);
        System.out.println(memberOptional);
        Member member = memberOptional.orElse(null);
        if (member != null) {
            return member;
        } else {
            log.info("Can not find member from database");
            return null;
        }
    }

    @Override
    @Transactional
    public MypageResponse callMyPage(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.orElse(null);
        if (member == null) {
            return null;
        }

        Optional<InProgressRoadmap> inProgressRoadmaps = (inProgressRoadmapRepository.findByMemberId(memberId));
        List<InProgressRoadmapDto> inProgressRoadmapDtos = new ArrayList<>();

        if(inProgressRoadmaps.isPresent()) {
            InProgressRoadmap inProgressRoadmap = inProgressRoadmaps.get();

            int totalNodeCount = (inProgressRoadmap.getInProgressNodes()).size();
            List<InProgressNode> inProgressNodes = inProgressNodeRepository.findByRoadmapAndDone(inProgressRoadmap.getRoadmap(), true);
            int doneNodeCount = inProgressNodes.size();

            double progress = Math.round(((double)doneNodeCount/totalNodeCount)*10000) / 100.0; //

            InProgressRoadmapDto inProgressRoadmapDto = InProgressRoadmapDto.builder()
                    .id(inProgressRoadmap.getId())
                    .title(inProgressRoadmap.getRoadmap().getTitle())
                    .thumbnail(inProgressRoadmap.getRoadmap().getThumbnailUrl())
                    .process(progress)
                    .build();
            inProgressRoadmapDtos.add(inProgressRoadmapDto);
        }

        return MypageResponse.builder()
                .memberId(memberId)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .bio(member.getBio())
                .avatarUrl(member.getAvatarUrl())
                .githubUrl(member.getGithubUrl())
                .blogUrl(member.getBlogUrl())
                .backjoonId(member.getBaekjoonId())
                .level(member.getLevel())
                .exp(member.getExp())
                .inProcessRoadmapDto(inProgressRoadmapDtos)
                .build();
    }


    @Override
    @Transactional
    public Boolean saveProfile(MypageRequest request) {
        //1. 비즈니스 로직 처리
        Member member = getLoggedInMember();
        if (!member.getId().equals(request.getMemberId())) {
            return false;
        }
        member.setBio(request.getBio());
        member.setNickname(request.getNickname());
        member.setBaekjoonId(request.getBaekjoonId());
        member.setBlogUrl(request.getBlogUrl());
        member.setGithubUrl(request.getGithubUrl());
        memberRepository.save(member);
        return true;
    }
}

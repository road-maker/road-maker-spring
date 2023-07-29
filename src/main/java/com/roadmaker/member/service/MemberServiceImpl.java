package com.roadmaker.member.service;

import com.roadmaker.comment.service.CommentService;
import com.roadmaker.commons.exception.NotFoundException;
import com.roadmaker.member.authentication.JwtProvider;
import com.roadmaker.member.dto.*;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.member.dto.TokenInfo;
import com.roadmaker.comment.dto.CommentDto;
import com.roadmaker.roadmap.dto.InProgressRoadmapDto;
import com.roadmaker.comment.entity.CommentRepository;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.inprogressroadmap.entity.InProgressRoadmap;
import com.roadmaker.inprogressroadmap.entity.InProgressRoadmapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void signUp(SignupRequest signupRequest) {
        Member member = signupRequest.toEntity(passwordEncoder);
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
    public boolean isDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.isPresent();
    }

    @Override
    public boolean isDuplicatedNickname(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);
        return member.isPresent();
    }

    public Member getLoggedInMember() {
        String email = SecurityUtil.getLoggedInMemberEmail();
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
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

        Member member = memberRepository.findById(memberId).orElse(null);

        List<InProgressRoadmap> inProgressRoadmaps = inProgressRoadmapRepository.findAllByMemberId(memberId);
        List<InProgressRoadmapDto> inProgressRoadmapDtos = new ArrayList<>();
        inProgressRoadmaps.forEach(

                inProgressRoadmap -> {
                    int totalNodeCount = (inProgressRoadmap.getInProgressNodes()).size();
                    int doneNodeCount = inProgressNodeRepository
                            .findByRoadmapAndDone(inProgressRoadmap.getRoadmap(), true).size();

                    double progress = Math.round(((double)doneNodeCount/totalNodeCount)*10000) / 100.0; //

                    InProgressRoadmapDto inProgressRoadmapDto = InProgressRoadmapDto.builder()
                            .id(inProgressRoadmap.getId())
                            .title(inProgressRoadmap.getRoadmap().getTitle())
                            .thumbnail(inProgressRoadmap.getRoadmap().getThumbnailUrl())
                            .process(progress)
                            .build();

                    inProgressRoadmapDtos.add(inProgressRoadmapDto);
                }
        );

        List<CommentDto> commentDtos = commentService.callMemberComment(memberId);

        return MypageResponse.builder()
                .memberId(memberId)
                .email(member.getEmail()) //memberId를 불러오는 과정에서 이미 null exception 예외 처리함
                .nickname(member.getNickname())
                .bio(member.getBio())
                .avatarUrl(member.getAvatarUrl())
                .githubUrl(member.getGithubUrl())
                .blogUrl(member.getBlogUrl())
                .backjoonId(member.getBaekjoonId())
                .level(member.getLevel())
                .exp(member.getExp())
                .inProcessRoadmaps(inProgressRoadmapDtos)
                .comments(commentDtos)
                .build();
    }

    @Override
    @Transactional
    public Boolean saveProfile(MypageRequest request, Member member) {
        //1. 비즈니스 로직 처리
        member.setBio(request.getBio());
        member.setNickname(request.getNickname());
        member.setBaekjoonId(request.getBaekjoonId());
        member.setBlogUrl(request.getBlogUrl());
        member.setGithubUrl(request.getGithubUrl());
        memberRepository.save(member);
        return true;
    }

    @Override
    public MemberResponse findMemberByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);

        if (member.isEmpty()) {
            return null;
        }

        return MemberResponse.of(member.get());
    }

    public MemberResponse findMemberByNickname(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);

        if(member.isEmpty()) {
            throw new NotFoundException();
        }

        return MemberResponse.of(member.get());
    }
}

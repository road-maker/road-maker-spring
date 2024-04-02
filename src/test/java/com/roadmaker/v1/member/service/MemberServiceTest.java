package com.roadmaker.v1.member.service;

import com.roadmaker.v1.image.dto.UploadImageResponse;
import com.roadmaker.v1.image.service.ImageService;
import com.roadmaker.v1.member.dto.request.MemberUpdateRequest;
import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.member.entity.MemberRepository;
import com.roadmaker.v1.member.exception.NicknameAlreadyRegisteredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ImageService imageService;

    @DisplayName("회원 프로필 정보를 변경할 수 있다.")
    @Test
    void updateProfile() {
        // given
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .nickname("새로운 닉네임")
                .bio("테스트 코드를 사랑합니다.")
                .blogUrl("https://techpedia.io")
                .githubUrl("https://github.com/onzerokang")
                .build();

        Member member = Member.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .nickname("기존 닉네임")
                .build();

        given(memberRepository.existsByNickname(request.nickname())).willReturn(false);

        // when
        memberService.updateProfile(request, member);

        //then
        assertThat(member.getNickname()).isEqualTo("새로운 닉네임");
        assertThat(member.getBio()).isEqualTo("테스트 코드를 사랑합니다.");
        assertThat(member.getBlogUrl()).isEqualTo("https://techpedia.io");
        assertThat(member.getGithubUrl()).isEqualTo("https://github.com/onzerokang");
    }

    @DisplayName("닉네임은 다른 사람이 사용중인 닉네임으로 변경할 수 없다.")
    @Test
    void updateProfileWithDuplicatedNickname() {
        // given
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .nickname("새로운 닉네임")
                .bio("테스트 코드를 사랑합니다.")
                .blogUrl("https://techpedia.io")
                .githubUrl("https://github.com/onzerokang")
                .build();

        Member member = Member.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .nickname("기존 닉네임")
                .build();

        given(memberRepository.existsByNickname(request.nickname())).willReturn(true);

        // when then
        assertThatThrownBy(() -> memberService.updateProfile(request, member))
                .isInstanceOf(NicknameAlreadyRegisteredException.class)
                .hasMessage("이미 등록된 닉네임입니다.");
    }

    @DisplayName("회원의 프로필 사진을 변경할 수 있다.")
    @Test
    void updateAvatarUrl() {
        // given
        Member member = Member.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .nickname("기존 닉네임")
                .build();

        String avatarUrl = "https://roadmaker-images.s3.amazonaws.com/1a9e014c-9870-4979-b20d-2187f3c73a63.webp";

        // when
        MemberAvatarUpdateResponse response = memberService.updateAvatarUrl(member, avatarUrl);

        //then
        assertThat(response.avatarUrl()).isEqualTo(avatarUrl);
        assertThat(member.getAvatarUrl()).isEqualTo(avatarUrl);
    }

}
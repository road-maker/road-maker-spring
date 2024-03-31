package com.roadmaker.v1.auth.service;

import com.roadmaker.v1.auth.dto.request.AuthSignupRequest;
import com.roadmaker.v1.auth.dto.response.AuthSignupResponse;
import com.roadmaker.v1.member.authentication.JwtProvider;
import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.member.entity.MemberRepository;
import com.roadmaker.v1.member.exception.EmailAlreadyRegisteredException;
import com.roadmaker.v1.member.exception.NicknameAlreadyRegisteredException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @DisplayName("회원가입 시 비밀번호는 암호화되며 엑세스 토큰을 반환한다.")
    @Test
    void signup() {
        // given
        AuthSignupRequest request = AuthSignupRequest.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .nickname("테스터")
                .build();

        Member member = new Member();

        ReflectionTestUtils.setField(member, "id", 1L);

        given(memberRepository.existsByEmail(request.email())).willReturn(false);
        given(memberRepository.existsByNickname(request.nickname())).willReturn(false);
        given(memberRepository.save(any(Member.class))).willReturn(member);

        given(jwtProvider.generate(eq(member.getId().toString()), any(Date.class))).willReturn("accessToken");

        given(passwordEncoder.encode(request.password())).willReturn("encodedPassword");

        // when
        AuthSignupResponse response = authService.signup(request);

        //then
        assertThat(response.accessToken()).isEqualTo("accessToken");
        then(passwordEncoder).should(times(1)).encode(request.password());
    }

    @DisplayName("회원 가입 시, 이미 등록된 이메일을 사용하면 회원 가입에 실패한다.")
    @Test
    void signupWithDuplicatedEmail() {
        // given
        AuthSignupRequest request = AuthSignupRequest.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .nickname("테스터")
                .build();

        given(memberRepository.existsByEmail(request.email())).willReturn(true);

        // when then
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(EmailAlreadyRegisteredException.class)
                .hasMessage("이미 등록된 이메일입니다.");
    }

    @DisplayName("회원 가입 시, 이미 등록된 닉네임을 사용하면 회원 가입에 실패한다.")
    @Test
    void signupWithDuplicatedNickname() {
        // given
        AuthSignupRequest request = AuthSignupRequest.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .nickname("테스터")
                .build();

        given(memberRepository.existsByNickname(request.nickname())).willReturn(true);

        // when then
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(NicknameAlreadyRegisteredException.class)
                .hasMessage("이미 등록된 닉네임입니다.");
    }

}
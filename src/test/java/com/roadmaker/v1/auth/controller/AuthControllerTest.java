package com.roadmaker.v1.auth.controller;

import com.roadmaker.v1.RestDocsSupport;
import com.roadmaker.v1.auth.dto.request.AuthLoginRequest;
import com.roadmaker.v1.auth.dto.request.AuthSignupRequest;
import com.roadmaker.v1.auth.dto.response.AuthLoginResponse;
import com.roadmaker.v1.auth.dto.response.AuthSignupResponse;
import com.roadmaker.v1.auth.exception.LoginFailedException;
import com.roadmaker.v1.auth.service.AuthService;
import com.roadmaker.v1.member.controller.MemberController;
import com.roadmaker.v1.member.exception.EmailAlreadyRegisteredException;
import com.roadmaker.v1.member.exception.NicknameAlreadyRegisteredException;
import com.roadmaker.v1.member.exception.UnAuthenticatedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends RestDocsSupport {

    @MockBean
    private AuthService authService;

    @Override
    protected Object initController() {
        return new AuthController(authService);
    }

    @DisplayName("회원 가입을 할 수 있다.")
    @Test
    void signup() throws Exception {
        // given
        AuthSignupRequest request = AuthSignupRequest.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .nickname("테스터")
                .build();

        AuthSignupResponse response = AuthSignupResponse.builder()
                .id(1L)
                .accessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
                .build();

        given(authService.signup(request)).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(post("/api/auth/signup")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("auth-signup",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                        .description("엑세스 토큰")
                        )
                ));
    }

    @DisplayName("이미 등록된 이메일로 회원 가입을 할 수 없다.")
    @Test
    void signupWithAlreadyRegisteredEmail() throws Exception {
        // given
        AuthSignupRequest request = AuthSignupRequest.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .nickname("테스터")
                .build();

        given(authService.signup(request)).willThrow(new EmailAlreadyRegisteredException());


        // when
        ResultActions result = mockMvc.perform(post("/api/auth/signup")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("auth-signup-duplicated-email",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                        .description("HTTP 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("에러 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.STRING)
                                        .description("에러 코드")
                        )
                ));
    }

    @DisplayName("이미 등록된 닉네임으로 회원 가입을 할 수 없다.")
    @Test
    void signupWithAlreadyRegisteredNickname() throws Exception {
        // given
        AuthSignupRequest request = AuthSignupRequest.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .nickname("테스터")
                .build();

        given(authService.signup(request)).willThrow(new NicknameAlreadyRegisteredException());


        // when
        ResultActions result = mockMvc.perform(post("/api/auth/signup")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("auth-signup-duplicated-nickname",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                        .description("HTTP 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("에러 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.STRING)
                                        .description("에러 코드")
                        )
                ));
    }

    @DisplayName("로그인 할 수 있다.")
    @Test
    void login() throws Exception {
        // given
        AuthLoginRequest request = AuthLoginRequest.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .build();

        AuthLoginResponse response = AuthLoginResponse.builder()
                .id(1L)
                .accessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
                .build();

        given(authService.login(request)).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                        .description("엑세스 토큰")
                        )
                ));
    }

    @DisplayName("로그인 실패")
    @Test
    void loginFail() throws Exception {
        // given
        AuthLoginRequest request = AuthLoginRequest.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .build();

        given(authService.login(request)).willThrow(new LoginFailedException());

        // when
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("auth-login-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                        .description("HTTP 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("에러 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.STRING)
                                        .description("에러 코드")
                        )
                ));
    }
}
package com.roadmaker.v1.member.controller;

import com.roadmaker.v1.RestDocsSupport;
import com.roadmaker.v1.auth.controller.AuthController;
import com.roadmaker.v1.auth.service.AuthService;
import com.roadmaker.v1.comment.service.CommentService;
import com.roadmaker.v1.member.dto.request.MemberUpdateRequest;
import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.member.exception.NicknameAlreadyRegisteredException;
import com.roadmaker.v1.member.service.MemberService;
import com.roadmaker.v1.roadmap.service.RoadmapService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends RestDocsSupport {

    @MockBean
    private MemberService memberService;

    @MockBean
    private RoadmapService roadmapService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private AuthService authService;

    @Override
    protected Object initController() {
        return new MemberController(memberService, roadmapService, commentService);
    }

    @DisplayName("회원 정보를 수정할 수 있다.")
    @Test
    void updateProfile() throws Exception {
        // given
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .nickname("테스터")
                .bio("테스트 코드를 사랑합니다.")
                .blogUrl("https://techpedia.io")
                .githubUrl("https://github.com/onzerokang")
                .build();

        // when
        ResultActions result = mockMvc.perform(patch("/api/members/profile")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-update-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("bio").type(JsonFieldType.STRING)
                                        .description("1줄 소개"),
                                fieldWithPath("githubUrl").type(JsonFieldType.STRING)
                                        .description("깃허브 주소"),
                                fieldWithPath("blogUrl").type(JsonFieldType.STRING)
                                        .description("블로그 주소")
                        )
                ));
    }

    @DisplayName("이미 등록된 닉네임으로 닉네임을 변경할 수 없다.")
    @Test
    void updateProfileWithAlreadyRegisteredNickname() throws Exception {
        // given
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .nickname("테스터")
                .bio("테스트 코드를 사랑합니다.")
                .blogUrl("https://techpedia.io")
                .githubUrl("https://github.com/onzerokang")
                .build();

        Member member = Member.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .nickname("테스터")
                .build();

        willThrow(new NicknameAlreadyRegisteredException())
                .given(memberService).updateProfile(any(MemberUpdateRequest.class), any(Member.class));

        // when
        ResultActions result = mockMvc.perform(patch("/api/members/profile")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("member-update-profile-duplicated-nickname",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("bio").type(JsonFieldType.STRING)
                                        .description("1줄 소개"),
                                fieldWithPath("githubUrl").type(JsonFieldType.STRING)
                                        .description("깃허브 주소"),
                                fieldWithPath("blogUrl").type(JsonFieldType.STRING)
                                        .description("블로그 주소")
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
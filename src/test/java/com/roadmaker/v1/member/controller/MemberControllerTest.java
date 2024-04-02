package com.roadmaker.v1.member.controller;

import com.roadmaker.v1.RestDocsSupport;
import com.roadmaker.v1.auth.controller.AuthController;
import com.roadmaker.v1.auth.service.AuthService;
import com.roadmaker.v1.comment.service.CommentService;
import com.roadmaker.v1.image.service.ImageService;
import com.roadmaker.v1.member.dto.request.MemberUpdateRequest;
import com.roadmaker.v1.member.dto.response.MemberFindResponse;
import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.member.exception.MemberNotFoundException;
import com.roadmaker.v1.member.exception.NicknameAlreadyRegisteredException;
import com.roadmaker.v1.member.service.MemberAvatarUpdateResponse;
import com.roadmaker.v1.member.service.MemberService;
import com.roadmaker.v1.roadmap.service.RoadmapService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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

    @MockBean
    private ImageService imageService;

    @Override
    protected Object initController() {
        return new MemberController(memberService, roadmapService, commentService, imageService);
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

    @DisplayName("회원 프로필 이미지를 업데이트한다.")
    @Test
    void uploadMemberAvatar() throws Exception {
        // given
        String avatarUrl = "https://roadmaker-images.s3.amazonaws.com/1a9e014c-9870-4979-b20d-2187f3c73a63.webp";

        Member member = Member.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .nickname("테스터")
                .build();

        MemberAvatarUpdateResponse response = MemberAvatarUpdateResponse.builder()
                .avatarUrl(avatarUrl)
                .build();

        given(imageService.uploadImage(any())).willReturn(avatarUrl);
        given(memberService.updateAvatarUrl(any(Member.class), anyString())).willReturn(response);

        MockMultipartFile mockImage = new MockMultipartFile(
                "file",
                "avatar.jpg",
                "image/jpeg",
                "avatar".getBytes()
        );

        // when
        ResultActions result = mockMvc.perform(multipart("/api/members/profile/avatar")
                .file(mockImage));

        //then
        result
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("member-avatar-upload",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(partWithName("file").description("회원 프로필 이미지")),
                        responseFields(
                                fieldWithPath("avatarUrl").type(JsonFieldType.STRING)
                                        .description("회원 프로필 이미지 URL")
                        )
                ));
    }

    @DisplayName("회원 ID로 회원을 조회한다.")
    @Test
    void findMember() throws Exception {
        // given
        long memberId = 1L;
        MemberFindResponse response = MemberFindResponse.builder()
                .id(1L)
                .email("tester@roadmaker.site")
                .nickname("테스터")
                .bio("테스트 코드를 사랑합니다")
                .avatarUrl("https://roadmaker-images.s3.amazonaws.com/1a9e014c-9870-4979-b20d-2187f3c73a63.webp")
                .blogUrl("https://techpedia.io")
                .githubUrl("https://github.com/onzerokang")
                .build();

        given(memberService.findMemberByMemberId(memberId)).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/members/{memberId}", memberId));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-find-by-id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("bio").type(JsonFieldType.STRING)
                                        .description("1줄 소개"),
                                fieldWithPath("avatarUrl").type(JsonFieldType.STRING)
                                        .description("회원 프로필 이미지 URL"),
                                fieldWithPath("githubUrl").type(JsonFieldType.STRING)
                                        .description("깃허브 주소"),
                                fieldWithPath("blogUrl").type(JsonFieldType.STRING)
                                        .description("블로그 주소")
                        )
                ));
    }

    @DisplayName("회원 ID와 일치하는 회원이 없을 경우 회원 조회에 실패한다.")
    @Test
    void findMemberWithMissMatchedId() throws Exception {
        // given
        long memberId = 1L;

        given(memberService.findMemberByMemberId(memberId)).willThrow(new MemberNotFoundException());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/members/{memberId}", memberId));

        //then
        result
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("member-find-by-id-not-found",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
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
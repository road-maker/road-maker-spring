package com.roadmaker.v1.roadmap.controller;

import com.roadmaker.v1.RestDocsSupport;
import com.roadmaker.v1.auth.service.AuthService;
import com.roadmaker.v1.comment.entity.Comment;
import com.roadmaker.v1.comment.service.CommentService;
import com.roadmaker.v1.image.service.ImageService;
import com.roadmaker.v1.inprogressroadmap.entity.InProgressRoadmapRepository;
import com.roadmaker.v1.like.service.LikeService;
import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.roadmap.dto.response.RoadmapCommentPagingResponse;
import com.roadmaker.v1.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.v1.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.v1.roadmap.service.RoadmapService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;
import software.amazon.awssdk.services.s3.endpoints.internal.Ref;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {RoadmapController.class})
class RoadmapControllerTest extends RestDocsSupport {

    @MockBean
    AuthService authService;

    @MockBean
    RoadmapService roadmapService;

    @MockBean
    ImageService imageService;

    @MockBean
    CommentService commentService;

    @MockBean
    LikeService likeService;

    @MockBean
    InProgressNodeRepository inProgressNodeRepository;

    @MockBean
    InProgressRoadmapRepository inProgressRoadmapRepository;

    @Override
    protected Object initController() {
        return new RoadmapController(
                authService,
                roadmapService,
                imageService,
                commentService,
                likeService,
                inProgressNodeRepository,
                inProgressRoadmapRepository
        );
    }

    @DisplayName("로드맵에 작성된 댓글을 가져올 수 있다.")
    @Test
    void findRoadmapComments() throws Exception {
        // given

        long roadmapId = 1L;
        long lastCommentId = 1L;
        int size = 20;

        Member member = Member.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .nickname("테스터")
                .avatarUrl("https://roadmaker-images.s3.amazonaws.com/1a9e014c-9870-4979-b20d-2187f3c73a63.webp")
                .build();

        Roadmap roadmap = Roadmap.builder()
                .title("자바 로드맵")
                .build();

        Comment comment = Comment.builder()
                .roadmap(roadmap)
                .member(member)
                .content("좋은 로드맵, 감사합니다!")
                .build();

        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(comment, "id", 1L);
        ReflectionTestUtils.setField(comment, "createdAt", LocalDateTime.of(2024, 4, 10, 17, 20, 30));

        RoadmapCommentPagingResponse response = RoadmapCommentPagingResponse.of(comment);

        given(roadmapService.findRoadmapComments(roadmapId, lastCommentId, size)).willReturn(List.of(response));

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders
                .get("/api/roadmaps/{roadmapId}/comments?lastCommentId={lastCommentId}&size={size}", roadmapId, lastCommentId, size));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("find-roadmap-comments",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("roadmapId").description("로드맵 ID")
                        ),
                        queryParameters(
                                parameterWithName("lastCommentId").description("마지막으로 읽어온 댓글 ID"),
                                parameterWithName("size").description("읽어올 댓글 개수(기본 값: 20)")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                        .description("댓글 ID"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING)
                                        .description("댓글 내용"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING)
                                        .description("댓글 작성 시간"),
                                fieldWithPath("[].member.id").type(JsonFieldType.NUMBER)
                                        .description("작성자 ID"),
                                fieldWithPath("[].member.nickname").type(JsonFieldType.STRING)
                                        .description("작성자 닉네임"),
                                fieldWithPath("[].member.avatarUrl").type(JsonFieldType.STRING)
                                        .description("작성자 프로필 이미지 URL")
                        )
                ));
    }
}
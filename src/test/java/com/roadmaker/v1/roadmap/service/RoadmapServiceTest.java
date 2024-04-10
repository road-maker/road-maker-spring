package com.roadmaker.v1.roadmap.service;

import com.roadmaker.v1.comment.entity.Comment;
import com.roadmaker.v1.comment.entity.CommentRepository;
import com.roadmaker.v1.image.service.ImageService;
import com.roadmaker.v1.inprogressroadmap.entity.InProgressRoadmapRepository;
import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.member.entity.MemberRepository;
import com.roadmaker.v1.roadmap.dto.response.RoadmapCommentPagingResponse;
import com.roadmaker.v1.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.v1.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.v1.roadmap.entity.roadmap.RoadmapRepository;
import com.roadmaker.v1.roadmap.entity.roadmapedge.RoadmapEdgeRepository;
import com.roadmaker.v1.roadmap.entity.roadmapviewport.RoadmapViewportRepository;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RoadmapServiceTest {
    @InjectMocks
    RoadmapService roadmapService;

    @Mock
    RoadmapRepository roadmapRepository;

    @Mock
    RoadmapEdgeRepository roadmapEdgeRepository;


    @Mock
    RoadmapViewportRepository roadmapViewportRepository;

    @Mock
    InProgressRoadmapRepository inProgressRoadmapRepository;

    @Mock
    InProgressNodeRepository inProgressNodeRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ImageService imageService;

    @DisplayName("로드맵의 댓글 목록을 조회할 수 있다.")
    @Test
    void findRoadmapComments() {
        // given
        Long roadmapId = 1L;
        Long lastCommentId = 0L;
        int size = 20;

        Member member = Member.builder()
                .email("tester@roadmaker.site")
                .password("road1234")
                .nickname("테스터")
                .avatarUrl("https://roadmaker-images.s3.amazonaws.com/1a9e014c-9870-4979-b20d-2187f3c73a63.webp")
                .build();

        Comment comment = Comment.builder()
                .member(member)
                .content("좋은 로드맵, 감사합니다!")
                .build();
        ReflectionTestUtils.setField(comment, "createdAt", LocalDateTime.of(2024, 4, 10, 17, 20, 30));

        List<Comment> mockComments = List.of(comment);
        Page<Comment> mockPage = new PageImpl<>(mockComments);
        given(commentRepository.findByIdGreaterThanAndRoadmapId(eq(lastCommentId), eq(roadmapId), any(Pageable.class)))
                .willReturn(mockPage);

        // when
        List<RoadmapCommentPagingResponse> roadmapComments = roadmapService.findRoadmapComments(roadmapId, lastCommentId, size);

        //then
        assertThat(roadmapComments)
                .extracting("content")
                .containsExactlyInAnyOrder("좋은 로드맵, 감사합니다!");

    }
}
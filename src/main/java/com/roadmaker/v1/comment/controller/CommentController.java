package com.roadmaker.v1.comment.controller;

import com.roadmaker.v1.comment.dto.request.CommentCreateRequest;
import com.roadmaker.v1.comment.service.CommentService;
import com.roadmaker.global.annotation.LoginMember;
import com.roadmaker.global.annotation.LoginRequired;
import com.roadmaker.v1.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @LoginRequired
    @PostMapping
    public ResponseEntity<HttpStatus> createComment(
            @LoginMember Member member,
            @Valid @RequestBody CommentCreateRequest commentCreateRequest
    ) {
        commentService.createComment(commentCreateRequest, member);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

package com.roadmaker.comment.controller;

import com.roadmaker.comment.dto.CommentDto;
import com.roadmaker.comment.service.CommentService;
import com.roadmaker.global.annotation.LoginMember;
import com.roadmaker.global.annotation.LoginRequired;
import com.roadmaker.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @LoginRequired
    @PostMapping
    public ResponseEntity<HttpStatus> saveComment(@LoginMember Member member, @RequestBody CommentDto commentReqeust) {
        commentService.saveComment(commentReqeust, member);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

package com.roadmaker.comment.controller;

import com.roadmaker.comment.dto.CommentDto;
import com.roadmaker.comment.service.CommentService;
import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.commons.exception.ConflictException;
import com.roadmaker.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @LoginRequired
    @PostMapping("/save-comment")
    public ResponseEntity<HttpStatus> saveComment (@LoginMember Member member, @RequestBody CommentDto commentReqeust) {
        // comment정보를 저장하고 저장하지 못했다면 Conflict 반환
        if(!commentService.saveComment(commentReqeust, member)) {
            throw new ConflictException("댓글 저장 실패");
        }

        // 무사히 완료하였다면 OK 반환
        return ResponseEntity.status(HttpStatus.OK).build();
        }
}

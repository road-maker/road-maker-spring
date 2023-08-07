package com.roadmaker.boj.controller;

import com.roadmaker.boj.service.CertifiedBojService;
import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/certified-bojs")
public class BojController {
    private final CertifiedBojService certifiedBojService;

    @LoginRequired
    @PostMapping("/checkProb")
    public Boolean certifyBlog(@RequestParam Long inProgressNodeId, @LoginMember Member member) {
        return certifiedBojService.certifyBoj(inProgressNodeId);
    }
}
package com.roadmaker.boj.controller;

import com.roadmaker.boj.dto.CertifiedBojRequest;
import com.roadmaker.boj.dto.CertifiedBojResponse;
import com.roadmaker.boj.service.CertifiedBojService;
import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/certified-bojs")
public class BojController {
    private final CertifiedBojService certifiedBojService;

    @LoginRequired
    @PostMapping("/check-prob")
    public CertifiedBojResponse certifyBlog(@RequestBody CertifiedBojRequest request, @LoginMember Member member) {
        return certifiedBojService.certifyBoj(request);
    }
}
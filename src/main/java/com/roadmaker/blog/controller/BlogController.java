package com.roadmaker.blog.controller;

import com.roadmaker.blog.dto.CertifiedBlogRequest;
import com.roadmaker.blog.dto.CertifiedBlogResponse;
import com.roadmaker.blog.service.CertifiedBlogService;
import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/certified-blogs")
public class BlogController {
    private final CertifiedBlogService certifiedBlogService;

    // 블로그 인증
    @LoginRequired
    @PostMapping("/submitUrl")
    public CertifiedBlogResponse certifyBlog(@RequestBody CertifiedBlogRequest request, @LoginMember Member member) {
        return certifiedBlogService.certifyBlog(request);
    }
}

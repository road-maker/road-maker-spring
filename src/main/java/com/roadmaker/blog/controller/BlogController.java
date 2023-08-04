package com.roadmaker.blog.controller;

import com.roadmaker.blog.dto.CertifiedBlogRequest;
import com.roadmaker.blog.dto.CertifiedBlogResponse;
import com.roadmaker.blog.service.CertifiedBlogService;
import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/certified-blogs")
public class BlogController {
    private final CertifiedBlogService certifiedBlogService;

    @LoginRequired
    @PostMapping("/setKeyword")
    public void setKeyword(@RequestParam String keyword, @LoginMember Member member) {
        certifiedBlogService.setKeyword(keyword);
    }

    // 블로그 인증
    @LoginRequired
    @PostMapping("/submitUrl")
    public CertifiedBlogResponse certifyBlog(@RequestBody CertifiedBlogRequest request, @LoginMember Member member) {
        certifiedBlogService.certifyBlog(request);
    }
}

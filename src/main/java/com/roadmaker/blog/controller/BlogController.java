package com.roadmaker.blog.controller;

import com.roadmaker.blog.dto.CertifiedBlogRequest;
import com.roadmaker.blog.dto.CertifiedBlogResponse;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlog;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlogRepository;
import com.roadmaker.blog.service.CertifiedBlogImpl;
import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.service.RoadmapServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/certified-blogs")
public class BlogController {
    private final CertifiedBlogRepository certifiedBlogRepository;

    // 블로그 인증 및 인증 생성
    @LoginRequired
    @PostMapping("/submitUrl")
    public CertifiedBlogResponse certifyBlog(@RequestBody CertifiedBlogRequest request, @LoginMember Member member) {
        String submitUrl = request.getSubmitUrl();
        String blogUrl = member.getBlogUrl();

        CertifiedBlogResponse checked= CertifiedBlogImpl.checkUrl(submitUrl, blogUrl);

        try {
            // Jsoup 라이브러리를 사용하여 블로그 콘텐츠 가져오기
            Document doc = Jsoup.connect(submitUrl).get();

            Elements blogContents = doc.select(".tt_article_useless_p_margin.contents_style");

            boolean keywordExists = false;
            // 블로그 콘텐츠에 해당 키워드가 있는지 대조한다.
            for (Element element : blogContents) {
                if (element.text().contains(keyword)) {
                    keywordExists = true;
                    break;
                }
            }

            // 인증에 성공하면 CertifiedBlog 엔티티를 저장합니다.
            if (keywordExists) {
                CertifiedBlog certifiedBlog = CertifiedBlog.builder()
                        .member(member)
                        .inProgressNode(inProgressNode)
                        .submitUrl(submitUrl)
                        .blogUrl(blogUrl)
                        .keyword(keyword)
                        .build();

                certifiedBlogRepository.save(certifiedBlog);

                return new CertifiedBlogResponse(submitUrl, true);
            }
        } catch (IOException e) {
            log.error("Error : {}", e.getMessage());
        }

        // 키워드가 존재하지 않거나 블로그 콘텐츠를 가져오는 데 오류가 있는 경우 false를 반환합니다.
        return new CertifiedBlogResponse(submitUrl, false);
    }
}

package com.roadmaker.blog.service;

import com.roadmaker.blog.dto.CertifiedBlogRequest;
import com.roadmaker.blog.dto.CertifiedBlogResponse;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlog;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertifiedBlogImpl implements CertifiedBlogService{
    private final

    @Value("")
    private String ipAddress;

    @Override
    public CertifiedBlogResponse checkUrl(String submitUrl, String blogUrl) {
        // 제출 URL에 프로토콜 접두사가 있는 경우 제거
        if (submitUrl.startsWith("https://")) {
            submitUrl = submitUrl.replaceFirst("https://", "");
        } else if (submitUrl.startsWith("http://")) {
            submitUrl = submitUrl.replaceFirst("http://", "");
        }

        // 제출한 URL이 인증된 회원의 블로그인지 확인
        if (!submitUrl.startsWith(blogUrl)) {   // 접두사가 회원의 블로그인 경우 확인 시작
            return new CertifiedBlogResponse(submitUrl, false);
        }
    }

    @Override
    public CertifiedBlogResponse CrawlSubmitBlog(CertifiedBlogRequest certifiedBlogRequest) {
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
}

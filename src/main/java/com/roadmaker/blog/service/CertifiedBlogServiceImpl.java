package com.roadmaker.blog.service;

import com.roadmaker.blog.dto.CertifiedBlogRequest;
import com.roadmaker.blog.dto.CertifiedBlogResponse;
import com.roadmaker.blog.entity.blogkeyword.BlogKeyword;
import com.roadmaker.blog.entity.blogkeyword.BlogKeywordRepository;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlog;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlogRepository;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CertifiedBlogServiceImpl implements CertifiedBlogService {
    private final InProgressNodeRepository inProgressNodeRepository;
    private final MemberRepository memberRepository;
    private final RoadmapNodeRepository roadmapNodeRepository;
    private final CertifiedBlogRepository certifiedBlogRepository;
    private final BlogKeywordRepository blogKeywordRepository;

    @Override
    @Transactional
    public void setKeyword(String keyword, RoadmapNode roadmapNodeId) {
        BlogKeyword blogKeyword = new BlogKeyword(roadmapNodeId, keyword);
        blogKeywordRepository.save(blogKeyword);
    }

    @Transactional
    public CertifiedBlogResponse certifyBlog(CertifiedBlogRequest request) {
        String submitUrl = request.getSubmitUrl();

        // TODO member테이블에 존재하는 필드 blogurl을 가져오는 로직
        // TODO blogkeyword table에 존재하는 keyword를 가져오는 로직

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

        try {
            // Jsoup 라이브러리를 사용하여 블로그 콘텐츠 가져오기
            Document doc = Jsoup.connect(submitUrl).get();

            Elements blogContents = doc.select(".tt_article_useless_p_margin.contents_style");

            boolean keywordExists = false;

            // 블로그 콘텐츠에 해당 키워드가 있는지 대조한다.
            for (org.jsoup.nodes.Element element : blogContents) {
                if (element.text().contains() {
                    keywordExists = true;
                    break;
                }
            }

            // 키워드가 존재하면 CertifiedBlog 엔티티를 저장합니다.
            if (keywordExists) {
                CertifiedBlog certifiedBlog = CertifiedBlog.builder()

                certifiedBlogRepository.save(certifiedBlog);

                return new CertifiedBlogResponse(submitUrl, true);
            }
        } catch (IOException e) {
            logger.error("Error : {}", e.getMessage());
        }

        // 키워드가 존재하지 않거나 블로그 콘텐츠를 가져오는 데 오류가 있는 경우 false를 반환합니다.
        return new CertifiedBlogResponse(submitUrl, false);
    }
}

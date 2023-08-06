package com.roadmaker.blog.service;

import com.roadmaker.blog.dto.CertifiedBlogRequest;
import com.roadmaker.blog.dto.CertifiedBlogResponse;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlog;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlogRepository;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.roadmap.entity.blogkeyword.BlogKeyword;
import com.roadmaker.roadmap.entity.blogkeyword.BlogKeywordRepository;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional
public class CertifiedBlogServiceImpl implements CertifiedBlogService {
    private final InProgressNodeRepository inProgressNodeRepository;
    private final MemberRepository memberRepository;
    private final CertifiedBlogRepository certifiedBlogRepository;
    private final RoadmapNodeRepository roadmapNodeRepository;
    private final BlogKeywordRepository blogKeywordRepository;


    @Override
    @Transactional
    public CertifiedBlogResponse certifyBlog(CertifiedBlogRequest request) {
        String submitUrl = request.getSubmitUrl();

        /// 멤버 ID로 blog_url 찾기
        Member member =
        String blogUrl = member.getBlogUrl();

        // 로드맵 노드 ID로 블로그 키워드 찾기
        RoadmapNode roadmapNode =
        BlogKeyword blogKeyword = blogKeywordRepository.findByRoadmapNodeId(request.getRoadmapNodeId());
        String keyword = blogKeyword.getKeyword();

        // 제출된 URL에서 프로토콜 접두사 제거
        if (submitUrl.startsWith("https://")) {
            submitUrl = submitUrl.replaceFirst("https://", "");
        } else if (submitUrl.startsWith("http://")) {
            submitUrl = submitUrl.replaceFirst("http://", "");
        }

        // 제출된 URL이 회원의 블로그에 속하는지 확인
        if (!submitUrl.startsWith(blogUrl)) {
            return new CertifiedBlogResponse(submitUrl, false);
        }

        try {
            // Jsoup 라이브러리를 사용하여 블로그 콘텐츠 가져오기
            Document doc = Jsoup.connect(submitUrl).get();

            Elements blogContents = doc.select(".tt_article_useless_p_margin.contents_style");

            boolean keywordExists = false;

            // 블로그 콘텐츠에 해당 키워드가 있는지 확인
            for (org.jsoup.nodes.Element element : blogContents) {
                if (element.text().contains(keyword)) {
                    keywordExists = true;
                    break;
                }
            }

            // 키워드가 있는 경우 CertifiedBlog 엔터티를 저장합니다.
            if (keywordExists) {
                CertifiedBlog certifiedBlog = CertifiedBlog.builder()
                        .member(member)
                        .inProgressNode(inProgressNodeRepository.getReferenceById())
                        .submitUrl(submitUrl)
                        .done(true)
                        .build();

                certifiedBlogRepository.save(certifiedBlog);

                return new CertifiedBlogResponse(submitUrl, true);
            }
        } catch (IOException e) {
            log.error("Error : {}", e.getMessage());
        }
        return new CertifiedBlogResponse(submitUrl, false);
    }
}

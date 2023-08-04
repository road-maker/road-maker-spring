package com.roadmaker.blog.service;

import com.roadmaker.blog.dto.CertifiedBlogRequest;
import com.roadmaker.blog.dto.CertifiedBlogResponse;
import com.roadmaker.blog.entity.blogkeyword.BlogKeywordRepository;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlog;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlogRepository;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
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

    @Transactional
    public void setKeyword(String keyword) {
        RoadmapNode roadmapNode = roadmapNodeRepository.findByRoadmapId(roadmapNodeId).orElse(null);
        if (roadmapNode != null) {
            BlogKeyword blogKeyword = BlogKeyword.builder()
                    .roadmapnode(roadmapNode)
                    .keyword(keyword)
                    .build();
            blogKeywordRepository.save(blogKeyword);
        } else {
            // roadmapNodeId에 해당하는 RoadmapNode를 찾지 못한 경우 예외 처리
            // 예를 들어, InvalidRoadmapNodeException 등을 던져서 해당 상황을 처리할 수 있습니다.
            throw new RuntimeException("Invalid Roadmap Node ID: " + roadmapNodeId);
        }
    }

    @Transactional
    public CertifiedBlogResponse certifyBlog(CertifiedBlogRequest request) {
        String submitUrl = request.getSubmitUrl();
        String keyword = request.getKeyword();
        InProgressNode inProgressNode = request.getInProgressNode();
        Member member = memberRepository.findById(inProgressNode.getId());
        String blogUrl = MemberRepository.

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
                if (element.text().contains(keyword)) {
                    keywordExists = true;
                    break;
                }
            }

            // 키워드가 존재하면 CertifiedBlog 엔티티를 저장합니다.
            if (keywordExists) {
                Member member = null; // 어떻게 Member 객체를 가져올지에 따라 초기화
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
            // 예외 처리
            // log.error("Error : {}", e.getMessage());
            e.printStackTrace();
        }

        // 키워드가 존재하지 않거나 블로그 콘텐츠를 가져오는 데 오류가 있는 경우 false를 반환합니다.
        return new CertifiedBlogResponse(submitUrl, false);
    }
}

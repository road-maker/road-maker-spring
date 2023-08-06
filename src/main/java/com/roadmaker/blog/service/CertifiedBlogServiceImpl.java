package com.roadmaker.blog.service;

import com.roadmaker.blog.dto.CertifiedBlogRequest;
import com.roadmaker.blog.dto.CertifiedBlogResponse;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlog;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlogRepository;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.roadmap.entity.blogkeyword.BlogKeyword;
import com.roadmaker.roadmap.entity.blogkeyword.BlogKeywordRepository;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional
public class CertifiedBlogServiceImpl implements CertifiedBlogService {
    private final Logger logger = LoggerFactory.getLogger(CertifiedBlogServiceImpl.class);
    private final InProgressNodeRepository inProgressNodeRepository;
    private final MemberRepository memberRepository;
    private final CertifiedBlogRepository certifiedBlogRepository;
    private final BlogKeywordRepository blogKeywordRepository;


    @Override
    @Transactional
    public CertifiedBlogResponse certifyBlog(CertifiedBlogRequest request) {
        String submitUrl = request.getSubmitUrl();

        // inprogressnodeid를 통해 member id와 roadmapnode id 찾기
        Long inProgressNodeId = request.getInProgressNodeId();
        InProgressNode inProgressNode = inProgressNodeRepository.findById(inProgressNodeId).orElse(null);

        // 멤버 ID로 member 엔티티 찾기
        Long memberId = inProgressNode != null ? inProgressNode.getMember().getId() : null;
        Member member = memberRepository.findById(memberId).orElse(null);

        String blogUrl = member != null ? member.getBlogUrl() : null;

        // 로드맵 노드 ID로 블로그 키워드 찾기
        Long roadmapNodeId = inProgressNode != null ? inProgressNode.getRoadmapNode().getId() : null;
        BlogKeyword blogKeyword = blogKeywordRepository.findByRoadmapNodeId(roadmapNodeId);

        String keyword = blogKeyword != null ? blogKeyword.getKeyword() : null;

        try {
            // Jsoup 라이브러리를 사용하여 블로그 콘텐츠 가져오기
            Document doc = Jsoup.connect(submitUrl).get();

            Elements blogContents = doc.select("#content");

            // 블로그 콘텐츠에 해당 키워드가 있는지 확인 && 본인 블로그인지 확인
            boolean keywordExists = blogContents.stream()
                    .anyMatch(element -> element.text().contains(keyword) && submitUrl.startsWith(blogUrl));

            // 키워드가 있는 경우 CertifiedBlog 엔터티를 저장합니다.
            if (keywordExists) {
                CertifiedBlog certifiedBlog = CertifiedBlog.builder()
                        .member(member)
                        .inProgressNode(inProgressNode)
                        .submitUrl(submitUrl)
                        .done(true)
                        .build();

                certifiedBlogRepository.save(certifiedBlog);
                return new CertifiedBlogResponse(submitUrl, true);
            }
        } catch (IOException e) {
            logger.error("Error : {}", e.getMessage());
        }
        return new CertifiedBlogResponse(submitUrl, false);
    }
}

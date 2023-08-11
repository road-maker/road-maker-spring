package com.roadmaker.blog.service;

import com.roadmaker.blog.dto.CertifiedBlogRequest;
import com.roadmaker.blog.dto.CertifiedBlogResponse;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlog;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlogRepository;
import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.entity.blogkeyword.BlogKeyword;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Optional;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional
public class CertifiedBlogServiceImpl implements CertifiedBlogService {
    private final Logger logger = LoggerFactory.getLogger(CertifiedBlogServiceImpl.class);
    private final InProgressNodeRepository inProgressNodeRepository;
    private final CertifiedBlogRepository certifiedBlogRepository;


    @Override
    @Transactional
    public CertifiedBlogResponse certifyBlog(CertifiedBlogRequest request) {
        if (request == null) {
            logger.error("Request is null.");
            throw new IllegalArgumentException("Request cannot be null.");
        }

        String submitUrl = request.getSubmitUrl();
        if (submitUrl == null || submitUrl.isEmpty()) {
            logger.error("Submit URL is invalid.");
            throw new IllegalArgumentException("Submit URL cannot be null or empty.");
        }

        Long inProgressNodeId = request.getInProgressNodeId();
        if (inProgressNodeId == null) {
            logger.error("InProgressNodeId is null.");
            throw new IllegalArgumentException("InProgressNodeId cannot be null.");
        }

        InProgressNode inProgressNode = inProgressNodeRepository.findById(inProgressNodeId).orElse(null);
        if (inProgressNode == null) {
            logger.error("InProgressNode not found for ID: {}", inProgressNodeId);
            return new CertifiedBlogResponse(submitUrl, false);
        }

        Member member = Optional.ofNullable(inProgressNode.getMember())
                .orElseThrow(() -> {
                    logger.error("Member not found.");
                    return new IllegalArgumentException("Member associated with InProgressNode is null.");
                });

        String blogUrl = member.getBlogUrl();
        if (blogUrl == null || blogUrl.isEmpty()) {
            logger.error("Blog URL for member is invalid.");
            throw new IllegalArgumentException("Blog URL cannot be null or empty.");
        }

        BlogKeyword blogKeyword = Optional.ofNullable(inProgressNode.getRoadmapNode().getBlogKeyword())
                .orElseThrow(() -> {
                    logger.error("BlogKeyword not found.");
                    return new IllegalArgumentException("BlogKeyword associated with InProgressNode is null.");
                });

        String keyword = blogKeyword.getKeyword();
        if (keyword == null || keyword.isEmpty()) {
            logger.error("Keyword is invalid.");
            throw new IllegalArgumentException("Keyword cannot be null or empty.");
        }

        Document doc = null;
        Elements blogContents = null;
        try {
            doc = Jsoup.connect(submitUrl).get();
        } catch (HttpStatusException e) {
            logger.error("HTTP error while connecting to the URL: {} - Status: {}", e.getUrl(), e.getStatusCode());
        } catch (UnsupportedMimeTypeException e) {
            logger.error("Unsupported MIME type: {}", e.getMimeType());
        } catch (SocketTimeoutException e) {
            logger.error("Connection timed out for URL: {}", submitUrl);
        } catch (IOException e) {
            logger.error("General IO error while connecting to the URL: {}", e.getMessage());
        }

        if (doc != null) {
            try {
                blogContents = doc.select(".article_view");
            } catch (Selector.SelectorParseException e) {
                logger.error("Invalid CSS selector used: {}", e.getMessage());
            }
        }

        if (blogContents != null && !blogContents.isEmpty()) {
            boolean keywordExists = blogContents.stream()
                    .anyMatch(element -> element.text().contains(keyword) && submitUrl.startsWith(blogUrl));

            if (keywordExists) {
                CertifiedBlog certifiedBlog = CertifiedBlog.builder()
                        .inProgressNode(inProgressNode)
                        .submitUrl(submitUrl)
                        .done(true)
                        .build();

                certifiedBlogRepository.save(certifiedBlog);
                return new CertifiedBlogResponse(submitUrl, true);
            }
        }

        return new CertifiedBlogResponse(submitUrl, false);
    }
}

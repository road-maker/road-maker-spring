package com.roadmaker.blog.service;

import com.roadmaker.blog.dto.CertifiedBlogRequest;
import com.roadmaker.blog.dto.CertifiedBlogResponse;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlog;
import com.roadmaker.blog.entity.certifiedblog.CertifiedBlogRepository;
import com.roadmaker.roadmap.entity.blogkeyword.BlogKeyword;
import com.roadmaker.roadmap.entity.blogkeyword.BlogKeywordRepository;
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

@Service @Slf4j
@RequiredArgsConstructor
@Transactional
public class CertifiedBlogServiceImpl implements CertifiedBlogService {
    private final Logger logger = LoggerFactory.getLogger(CertifiedBlogServiceImpl.class);
    private final CertifiedBlogRepository certifiedBlogRepository;
    private final BlogKeywordRepository blogKeywordRepository;

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

        Long blogKeywordId = request.getBlogKeywordId();
        if (blogKeywordId == null) {
            logger.error("BlogKeywordId is null.");
            throw new IllegalArgumentException("BlogKeywordId cannot be null.");
        }

        // Assuming you have a BlogKeywordRepository and a method to fetch the keyword by its ID.
        BlogKeyword blogKeyword = blogKeywordRepository.findById(blogKeywordId).orElse(null);
        if (blogKeyword == null) {
            logger.error("BlogKeyword not found for ID: {}", blogKeywordId);
            return new CertifiedBlogResponse(submitUrl, false);
        }

        String keyword = blogKeyword.getKeyword();

        Document doc = null;
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
            Elements blogContents;
            try {
                blogContents = doc.select(".article_view");
                boolean keywordExists = blogContents.stream()
                        .anyMatch(element -> element.text().contains(keyword));

                if (keywordExists) {
                    CertifiedBlog certifiedBlog = CertifiedBlog.builder()
                            .submitUrl(submitUrl)
                            .done(true)
                            .build();

                    certifiedBlogRepository.save(certifiedBlog);
                    return new CertifiedBlogResponse(submitUrl, true);
                }
            } catch (Selector.SelectorParseException e) {
                logger.error("Invalid CSS selector used: {}", e.getMessage());
            }
        }

        return new CertifiedBlogResponse(submitUrl, false);
    }
}

package com.roadmaker.boj.service;

import com.roadmaker.blog.service.CertifiedBlogServiceImpl;
import com.roadmaker.boj.dto.CertifiedBojRequest;
import com.roadmaker.boj.dto.CertifiedBojResponse;
import com.roadmaker.boj.entity.certifiedboj.CertifiedBoj;
import com.roadmaker.boj.entity.certifiedboj.CertifiedBojRepository;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.roadmap.entity.bojprob.BojProb;
import com.roadmaker.roadmap.entity.bojprob.BojProbRepository;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional
public class CertifiedBojService {
    private final Logger logger = LoggerFactory.getLogger(CertifiedBojService.class);
    private final InProgressNodeRepository inProgressNodeRepository;
    private final BojProbRepository bojProbRepository;
    private final MemberRepository memberRepository;
    private final CertifiedBojRepository certifiedBojRepository;

    public CertifiedBojResponse certifyBoj(@RequestBody CertifiedBojRequest request) {
        Long inProgressNodeId = request.getInProgressNodeId();
        InProgressNode inProgressNode = inProgressNodeRepository.findById(inProgressNodeId).orElse(null);

        // 진행중인 노드를 통해 로드맵 노드의 Id 찾기
        Long roadmapNodeId = inProgressNode != null ? inProgressNode.getRoadmapNode().getId() : null;
        BojProb bojProb = bojProbRepository.findByRoadmapNodeId(roadmapNodeId);

        // 멤버 ID로 member 엔티티 찾기
        Long memberId = inProgressNode != null ? inProgressNode.getMember().getId() : null;
        Member member = memberRepository.findById(memberId).orElse(null);

        String baekjoonId = member != null ? member.getBaekjoonId() : null;

        // 노드에서 bojProb 참조해서 원하는 값 추출
        String probTitle = bojProb != null ? bojProb.getBojTitle() : null;
        String probNumber = bojProb != null ? bojProb.getBojNumber() : null;
        String probNumString = String.valueOf(probNumber);

        CertifiedBojResponse certifiedBojResponse = BojAlreadyDone(request);
        if (certifiedBojResponse != null) {
            return certifiedBojResponse;
        }

        try {
            // Jsoup을 이용하여 해당 웹 페이지에 연결
            Document doc = Jsoup.connect(String.format("https://www.acmicpc.net/user/%s",baekjoonId)).get();

            // 원하는 정보가 있는 특정 클래스(class) 선택
            String targetClass = "problem-list"; // 대상 클래스 이름으로 변경해야 합니다.

            // 해당 클래스를 가진 요소 선택
            Element classElement = doc.getElementsByClass(targetClass).first();

            // 해당 클래스의 모든 하위 요소들 선택
            Elements elements = classElement.getElementsByTag("a");

            // 원하는 정보가 들어있는 요소 추출 및 처리
            List<Integer> dataToCompare = new ArrayList<>();
            for (Element element : elements) {
                // 예시로 해당 요소의 텍스트를 가져와서 정수로 변환하여 리스트에 추가합니다.
                String elementText = element.text();
                int value = Integer.parseInt(elementText.trim());
                dataToCompare.add(value);
            }

            // 이분 탐색으로 대조
            int targetValue = Integer.parseInt(probNumString);
            int low = 0;
            int high = dataToCompare.size() - 1;
            while (low <= high) {
                int mid = low + (high - low) / 2;
                int midValue = dataToCompare.get(mid);

                if (midValue == targetValue) {
                    // 찾았으면 true를 반환하고 메서드를 종료합니다.
                    CertifiedBoj certifiedBoj = CertifiedBoj.builder()
                            .inProgressNode(inProgressNode)
                            .done(true)
                            .build();
                    certifiedBojRepository.save(certifiedBoj);
                    return new CertifiedBojResponse(probNumString,probTitle,true);
                } else if (midValue < targetValue) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        } catch (Exception e) {
            logger.debug("Error:", e);
        }
        // 원하는 정보를 찾지 못했을 경우 false를 반환합니다.
        return new CertifiedBojResponse(probNumString,probTitle,false);
    }

    // 인증이 저장되어있는지 확인하는 로직
    private CertifiedBojResponse BojAlreadyDone(CertifiedBojRequest request) {
        CertifiedBoj certifiedBoj = certifiedBojRepository.findById(request.getInProgressNodeId()).orElse(null);
        if (certifiedBoj != null) {
            BojProb bojProb = bojProbRepository.findByRoadmapNodeId(certifiedBoj.getInProgressNode().getRoadmapNode().getId());
            return new CertifiedBojResponse(bojProb.getBojNumber(), bojProb.getBojTitle(), true);
        }
        return null;
    }
}

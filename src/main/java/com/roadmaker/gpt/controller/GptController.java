package com.roadmaker.gpt.controller;

import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.gpt.service.GptService;
import com.roadmaker.gpt.dto.NodeDetail;
import com.roadmaker.gpt.dto.RoadmapData;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController @Slf4j @RequiredArgsConstructor
@RequestMapping("/api/gpt")
public class GptController {

    private final GptService gptService;

    @PostMapping("/roadmap")
    public List<RoadmapData> getRoadmapDraft(@RequestParam @NotBlank String prompt) {
        // 명령
        String content1 = "With every reliable and valid development and computer science knowledge, construct a roadmap for developers. All your answers must be in Korean." +
                "Your answer('message.content') must contain two parts. First part is the roadmap you must make, answer in a format as the following example." +
                "\"1?title\n1.1?item1\n1.1.1?subItem1\n1.1.2?subItem2\n1.1.3?subItem3\n1.2?item2\n1.2.1?subItem1\n1.2.2?subItem2\n1.2.3?subItem3\n1.3?item3\n1.3.1?subItem1\n1.3.2?subItem2\n1.4?item4\n1.4.1?subItem1\n2?title\n2.1?item1\n2.1?item2\n2.3?item3\n2.4?item4\n2.4.1?subItem1\n3?title\n3.1?item1\n3.2?item2\n3.3?item3\n3.4?item4\n4?title\n4.1?item1\n4.2?item2\n4.3?item3\n4.4?item4\n\"" +
                "Second, explain(It is description for roadmap, not roadmap itself) in one line(but can be multiple sentences) three things(expectations of this roadmap, further recommended subjects to study, and explanation of the reason why you should study each contents of the roadmaps) in given format.\"0.1?explanation1\n0.2?explanation2\n0.3?explanation3\n\"" +
                "It is strongly recommended to you to add or delete titles, items or layers for adequate need to guide people with given subject(You must make at least 3 titles for given keyword)." +
                "Layer structures are essential to this roadmap. For example, 1.1, 1.2, 1.3 and 1.4 are all related to and belong to upper layer 1";
        String content2 = String.format("Make roadmap for '%s'", prompt);

        // gpt 요청 객체 생성 후 응답 받기
        String gptCompletion = gptService.getGptAnswer(content1, content2);
        // 응답 내용 파싱
        return gptService.messageParsing(gptCompletion);
    }

    // 멀티스레드로 노드 설명들 추가하는 api
    @PostMapping("/roadmap/detail")
    public List<NodeDetail> detailContentsThread(@RequestBody @NotBlank List<HashMap<String, Object>> draft) {
        List<RoadmapData> roadmapData = new ArrayList<>();
        draft.forEach(dataset -> {
            RoadmapData data = new RoadmapData((String) dataset.get("id"), (String) dataset.get("content"));
            roadmapData.add(data);
        });
        return gptService.makeDetailsAuto(roadmapData);
    }

    @LoginRequired
    @PostMapping("/detail")
    public NodeDetail detailContent(@RequestParam @NotBlank String course){
        return gptService.makeDetails(course);
    }
}

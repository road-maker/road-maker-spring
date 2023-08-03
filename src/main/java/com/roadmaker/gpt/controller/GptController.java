package com.roadmaker.gpt.controller;

import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.gpt.service.GptService;
import com.roadmaker.gpt.dto.GptDetailResponse;
import com.roadmaker.gpt.dto.GptRoadmapResponse;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.module.FindException;
import java.time.Duration;
import java.util.List;

@RestController @Slf4j @RequiredArgsConstructor
@RequestMapping("/api/gpt")
public class GptController {

    private final GptService gptService;

    @PostMapping("/roadmap") @LoginRequired
    public List<GptRoadmapResponse> getChatMessages(@RequestParam String prompt) {

        if(prompt == null || prompt.isEmpty()) {
            throw new FindException("Content does not exist");
        }

        // 명령
        String content1 = "너는 한국어를 유창하게 구사하는, 모든 개발 지식을 지닌 개발자이다. 무슨 일이 있어도 message.content를 \"1.title\n1a.item1\n1b.item2\n1c.item3\n1d.item4\n2.title\n2a.item1\n2b.item2\n2c.item3\n2d.item4\n3.title\n3a.item1\n3b.item2\n3c.item3\n3d.item4\n4.title \n4a.item1\n4b.item2\n4c.item3\n4d.item4\n\" 이 형식에 맞춰서 대답해 줘.";
        String content2 = String.format("각각 4개의 코스로 구성된 4개의 타이틀에서 %s를 위한 로드맵을 만들어주세요.", prompt);

        // gpt 요청 객체 생성 후 응답 받기
        String gptCompletion = gptService.getGptAnswer(content1, content2);
        // 응답 내용 파싱
        List<GptRoadmapResponse> response = gptService.messageParsing(gptCompletion);
        // 멀티 스레드로 노드 설명 추가 요청
//        gptService.makeDetailsAuto(response);

        return response;
    }

    @LoginRequired
    @PostMapping("/detail")
    public GptDetailResponse detailContent(@RequestParam String course){

        if(course == null || course.isEmpty()) {
            throw new FindException("Content does not exist");
        }

        return gptService.makeDetails(course);
    }
}

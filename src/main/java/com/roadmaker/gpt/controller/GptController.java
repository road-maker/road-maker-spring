package com.roadmaker.gpt.controller;

import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.gpt.dto.GptDetailResponse;
import com.roadmaker.gpt.dto.GptRoadmapResponse;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/gpt")
public class GptController {
    private final String OPENAI_TOKEN;
    private static final Duration DURATION = Duration.ofSeconds(120);

    public GptController(@Value("${gpt.api-key}") String apiKey) {
        this.OPENAI_TOKEN = apiKey;
    }

    @PostMapping("/roadmap")
    public List<GptRoadmapResponse> getChatMessages(@RequestParam String prompt) {

        OpenAiService service = new OpenAiService(OPENAI_TOKEN, DURATION);
        List<ChatMessage> messages = new ArrayList<>();

        String content1;
        String content2;

        content1 = "너는 한국어를 유창하게 구사하는, 모든 개발 지식을 지닌 개발자이다. 반드시 message.content를 \"1.title\n1a.item1\n1b.item2\n1c.item3\n1d.item4\n2.title\n2a.item1\n2b.item2\n2c.item3\n2d.item4\n3.title\n3a.item1\n3b.item2\n3c.item3\n3d.item4\n4.title \n4a.item1\n4b.item2\n4c.item3\n4d.item4\n\" 이 형식에 맞춰서 대답해주세요.";
        content2 = String.format("각각 4개의 코스로 구성된 4개의 타이틀에서 %s를 위한 로드맵을 만들어주세요.", prompt);

        ChatMessage message1 = new ChatMessage("system", content1 );
        ChatMessage message2 = new ChatMessage("user", content2 );

        messages.add(message1);
        messages.add(message2);

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder().messages(messages)
                .model("gpt-3.5-turbo").build();

        String gptCompletion = service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();

        // gptCompletion 문자열을 파싱합니다.
        String[] lines = gptCompletion.split("\n");
        List<GptRoadmapResponse> formattedGptRoadmapResponses = new ArrayList<>();

        for (String line : lines) {
            // 공백을 제거합니다.
            line = line.trim();

            // 각 line에 점(.) 문자가 포함되어 있는지 확인
            if (line.contains(".")) {
                // 점을 기준으로 줄을 ID와 콘텐츠로 분할합니다.
                String[] parts = line.split("\\.", 2); // Split into two parts at the first dot

                // parts에서 ID와 content를 추출합니다. (trim은 공백 제거)
                String id = parts[0].trim();
                String content = parts[1].trim();

                // 새 문장을 만들어 formattedGptRoadmapResponses에 추가합니다.
                GptRoadmapResponse gptRoadmapResponse = new GptRoadmapResponse(id, content);
                formattedGptRoadmapResponses.add(gptRoadmapResponse);
            }
        }

        return formattedGptRoadmapResponses;
    }

    @LoginRequired
    @PostMapping("/detail")
    public GptDetailResponse detailContent(@RequestParam String course){
        OpenAiService service = new OpenAiService(OPENAI_TOKEN, DURATION);
        List<ChatMessage> messages = new ArrayList<>();

        String content1 = "너는 한국어를 매우 잘하는, 모든 개발 지식을 가지고 있는 개발자이다. 입력 값의 개념을 반드시 한 단락으로 설명해줘. 단 네가 설명할 개념을 innerHTML을 이용해 보여줄 예정이니, <h1>, <p>, <b>, <ul>, <li> 태그를 적절히 활용해서 포맷팅해줘. 그리고 개행문자는 넣지 말아줘";
        String content2 = String.format(course);

        ChatMessage message1 = new ChatMessage("system", content1 );
        ChatMessage message2 = new ChatMessage("user", content2 );

        messages.add(message1);
        messages.add(message2);

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder().messages(messages)
                .model("gpt-3.5-turbo").build();

        return new GptDetailResponse(service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent());
    }
}

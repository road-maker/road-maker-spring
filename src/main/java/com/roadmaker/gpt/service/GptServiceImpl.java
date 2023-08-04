package com.roadmaker.gpt.service;

import com.roadmaker.gpt.dto.GptDetailResponse;
import com.roadmaker.gpt.dto.GptRoadmapResponse;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class GptServiceImpl implements GptService {

    private final OpenAiService service;

    private static final Duration DURATION = Duration.ofSeconds(120);

    public GptServiceImpl(@Value("${gpt.api-key}") String apiKey) { //
        this.service = new OpenAiService(apiKey, DURATION);
    }

    private List<ChatMessage> getMessage(String content1, String content2) {

        List<ChatMessage> messages = new ArrayList<>();

        ChatMessage message1 = new ChatMessage("system", content1 );
        ChatMessage message2 = new ChatMessage("user", content2 );

        messages.add(message1);
        messages.add(message2);

        return messages;
    }
    public String getGptAnswer(String content1, String content2) {
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(getMessage(content1, content2))
                .model("gpt-3.5-turbo")
                .build();

        // 받은 데이터
        return service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
    }

    public List<GptRoadmapResponse> messageParsing(String gptResponse) {
        // gptCompletion 문자열을 파싱합니다.
        String[] lines = gptResponse.split("\n");

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

                if(!id.matches(".*\\d.*")) {
                    continue;
                }
                // 새 문장을 만들어 formattedGptRoadmapResponses에 추가합니다.
                GptRoadmapResponse gptRoadmapResponse = new GptRoadmapResponse(id, content);
                formattedGptRoadmapResponses.add(gptRoadmapResponse);
            }
        }
        return formattedGptRoadmapResponses;
    }

    public GptDetailResponse makeDetails (String course) {
        String content1 = "너는 한국어를 매우 잘하는, 모든 개발 지식을 가지고 있는 개발자이다. 응답은 반드시 한 단락으로 해줘.";
        String content2 = String.format(course);

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(getMessage(content1, content2))
                .model("gpt-3.5-turbo")
                .build();

        return new GptDetailResponse(service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent());
    }
//    public String makeDetailsAuto (List<GptRoadmapResponse> response) {
//
//        response.forEach( individualResponse -> {
//            String id = individualResponse.getId();
//            String course = individualResponse.getContent();
//
//
//
//        });
//
//        // 자동으로 노드의 설명을 만들어주기
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(20);
//        executor.initialize();
//
//        Runnable callGptApi = () -> {
//            try {
//                gptService.detailContentAuto(id, content);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        };
//
//        for(int i = 0; i < 10; i ++ ) {
//            executor.execute(callGptApi);
//        }

}


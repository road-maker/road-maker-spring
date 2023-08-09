package com.roadmaker.gpt.service;

import com.roadmaker.gpt.dto.NodeDetail;
import com.roadmaker.gpt.dto.RoadmapData;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GptServiceImpl implements GptService {

    // 스레드 풀 관리
    @Autowired
    ThreadPoolTaskExecutor executor;

    private final OpenAiService service;

    private final String gptModel = "gpt-3.5-turbo";

    private static final Duration DURATION = Duration.ofSeconds(120);

    public GptServiceImpl(@Value("${gpt.api-key}") String apiKey) { //
        this.service = new OpenAiService(apiKey, DURATION);
    }

    private List<ChatMessage> getMessage(String content1, String content2) {

        List<ChatMessage> messages = new ArrayList<>();

        ChatMessage message1 = new ChatMessage("system", content1);
        ChatMessage message2 = new ChatMessage("user", content2);

        messages.add(message1);
        messages.add(message2);

        return messages;
    }

    public String getGptAnswer(String content1, String content2) {
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(getMessage(content1, content2))
                .model(gptModel)
                .temperature(0.2)
                .build();

        // 받은 데이터
        return service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
    }

    public List<RoadmapData> messageParsing(String gptResponse) {
        // gptCompletion 문자열을 파싱합니다.
        String[] lines = gptResponse.split("\n");

        List<RoadmapData> formattedRoadmapRespons = new ArrayList<>();
        for (String line : lines) {
            // 공백을 제거합니다.
            line = line.trim();

            // 각 line에 점(.) 문자가 포함되어 있는지 확인
            if (line.contains("?")) {
                // 점을 기준으로 줄을 ID와 콘텐츠로 분할합니다.
                String[] parts = line.split("\\?", 2); // Split into two parts at the first dot

                // parts에서 ID와 content를 추출합니다. (trim은 공백 제거)
                String id = parts[0].trim();
                String content = parts[1].trim();

                System.out.println("Content: " + content);

//                if (id.length() > 3) {
//                    continue;
//                }
                // 새 문장을 만들어 formattedGptRoadmapResponses에 추가합니다.
                RoadmapData roadmapData = new RoadmapData(id, content);
                formattedRoadmapRespons.add(roadmapData);
                System.out.println("roadmapData:" + roadmapData);
            }
        }
        return formattedRoadmapRespons;
    }

    //개별 노드의 설명을 생성하는 요청
    public NodeDetail makeDetails(String course) {
        String content1 =
                "With every reliable and valid development and computer science knowledge, explain the concept of the provided argument. All your answers must be in Korean." +
                "Your answer should be in innerHTML format and you should only use tags <h1>, <p>, <b>, <ul> and <li>. Your answers should only be in paragraphs.";
        String content2 = String.format(course);

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(getMessage(content1, content2))
                .model(gptModel)
                .build();

        String output = newlineCharacterReplacer(service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent());
        return new NodeDetail(output);
    }

    // 한번에 여러 개의 노드의 gpt 설명을 추가하기 위한 메서드
    public NodeDetail makeDetails(String course, String id) {
        String content1 =
                "With every reliable and valid development and computer science knowledge, explain the concept of the provided argument. All your answers must be in Korean." +
                "Your answer should be in innerHTML format and you should only use tags <h1>, <p>, <b>, <ul> and <li>. Your answers should only be in paragraphs.";
        String content2 = String.format(course);

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(getMessage(content1, content2))
                .model(gptModel)
                .build();

        String output = newlineCharacterReplacer(service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent());
        return new NodeDetail(output, id);
    }

    private String newlineCharacterReplacer(String input) {
        String codeBlockRegex = "<code>(.*?)</code>";
        Pattern codeBlockPattern = Pattern.compile(codeBlockRegex, Pattern.DOTALL);
        Matcher codeBlockMatcher = codeBlockPattern.matcher(input);

        StringBuilder output = new StringBuilder();
        int lastEnd = 0;
        while (codeBlockMatcher.find()) {
            String beforeCodeBlock = input.substring(lastEnd, codeBlockMatcher.start());
            String codeBlockContent = codeBlockMatcher.group(1);
            output.append(beforeCodeBlock.replace("\n\n", "\n"));
            output.append("<code>");
            output.append(codeBlockContent);
            output.append("</code>");
            lastEnd = codeBlockMatcher.end();
        }
        output.append(input.substring(lastEnd).replace("\n\n", "\n"));

        return output.toString();
    }

    @Async("executor")
    private List<Future<NodeDetail>> makeDetailsThread(List<RoadmapData> response) {

        List<Future<NodeDetail>> futures = new ArrayList<>();

        // Callable: 비동기 작업의 결과물인 Future 클래스 형태로 반환함
        class Task implements Callable<NodeDetail> { // 보다 복잡한 task를 정의
            private final String course;
            private final String id;
            public Task(String course, String id) {
                this.course = course;
                this.id = id;
            }

            @Override
            public NodeDetail call() throws Exception {
                return makeDetails(course, id);
            }
        }

        for(RoadmapData individualResponse : response) {
            String id = individualResponse.getId();
            String course = individualResponse.getContent();

            Future<NodeDetail> future = executor.submit(new Task(course,id));
            futures.add(future);
        }

        return futures;
    }

    public List<NodeDetail> makeDetailsAuto(List<RoadmapData> response) {
        // synchronizedList: 주어진 리스트를 동기화된 리스트로 변환. 여러 스레드에서 접근 가능, gptResponse.add를 통하여 결과 추가 가능
        // 성능이 떨어질 수 있음! 참고 keyword: Java의 동기화된 컬렉션
        List<NodeDetail> detailResponses = Collections.synchronizedList(new ArrayList<>());
        List<Future<NodeDetail>> futures = makeDetailsThread(response);
        //Future형태의 데이터를 반환해야하는 형태의 데이터로 변환
        for(Future<NodeDetail> future : futures) {
            try{
                NodeDetail gptResponse = future.get();
                detailResponses.add(gptResponse);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("스레드 중 예외 발생", e.getCause());
            } catch (ExecutionException e) {
                throw new RuntimeException("스레드 중 예외 발생", e.getCause());
            }
        }
        return detailResponses;
    }
}


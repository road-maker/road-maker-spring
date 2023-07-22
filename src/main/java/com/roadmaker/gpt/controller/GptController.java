package com.roadmaker.gpt.controller;

import com.roadmaker.gpt.dto.ChatMessagePrompt;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
public class GptController {

    private final String OPENAI_TOKEN;
    private static final Duration DURATION = Duration.ofSeconds(30);

    public GptController(@Value("${gpt.api-key}") String apiKey) {
        this.OPENAI_TOKEN = apiKey;
    }

    @GetMapping("/getChat/{prompt}")
    public String getPrompt(@PathVariable String prompt) {
        OpenAiService service = new OpenAiService(OPENAI_TOKEN, DURATION);
        CompletionRequest completionRequest = CompletionRequest.builder().prompt(prompt).model("text-davinci-003")
                .echo(true).build();

        return service.createCompletion(completionRequest).getChoices().get(0).getText();
    }

    @PostMapping("/chat")
    public String getChatMessages(@RequestBody ChatMessagePrompt prompt) {

        OpenAiService service = new OpenAiService(OPENAI_TOKEN, DURATION);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder().messages(prompt.getChatMessage())
                .model("gpt-3.5-turbo").build();

        return service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
    }

}
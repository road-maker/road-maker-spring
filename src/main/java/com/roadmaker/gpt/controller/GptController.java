package com.roadmaker.gpt.controller;

import com.roadmaker.gpt.dto.ChatMessagePrompt;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GptController {

    private final String OPENAI_TOKEN;
    private static final Duration DURATION = Duration.ofSeconds(60);

    public GptController(@Value("${gpt.api-key}") String apiKey) {
        this.OPENAI_TOKEN = apiKey;
    }

    @GetMapping("/getChat")
    public String getPrompt(@RequestParam String prompt) {
        OpenAiService service = new OpenAiService(OPENAI_TOKEN, DURATION);

        String message = String.format("%s",prompt);

        CompletionRequest completionRequest = CompletionRequest.builder().prompt(message).model("text-davinci-003")
                .echo(true).build();
//                .maxTokens(10000000)

        return service.createCompletion(completionRequest).getChoices().get(0).getText();
    }

    @PostMapping("/chat")
    public String getChatMessages(@RequestParam String prompt) {

//        기존에 받던 Body argument : @RequestBody ChatMessagePrompt prompt
        OpenAiService service = new OpenAiService(OPENAI_TOKEN, DURATION);
        List<ChatMessage> messages = new ArrayList<>();

        String content1 = "you are a senior developer. You must return only message.content in the form \"1.title\n-item1\nitem2\nitem3\nitem4\n2.title\n-item1\nitem2\nitem3\nitem4\n3.title\n-item1\nitem2\nitem3\nitem4\n4.title \n-item1\nitem2\nitem3\nitem4\n\"";
        String content2 = String.format("Create a roadmap to %s in four title, each with four courses.",prompt);

        ChatMessage message1 = new ChatMessage("system", content1 );
        ChatMessage message2 = new ChatMessage("user", content2 );

        messages.add(message1);
        messages.add(message2);

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder().messages(messages)
                .model("gpt-3.5-turbo").build();

        return service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
    }

}

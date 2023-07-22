package com.roadmaker.gpt.controller;
import com.roadmaker.gpt.dto.Sentence;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/chat")
    public List<Sentence> getChatMessages(@RequestParam String prompt) {

        OpenAiService service = new OpenAiService(OPENAI_TOKEN, DURATION);
        List<ChatMessage> messages = new ArrayList<>();

        String content1 = "you are a senior developer. You must return only message.content in the form \"1.title\n1a.item1\n1b.item2\n1c.item3\n1d.item4\n2.title\n2a.item1\n2b.item2\n2c.item3\n2d.item4\n3.title\n3a.item1\n3b.item2\n3c.item3\n3d.item4\n4.title \n4a.item1\n4b.item2\n4c.item3\n4d.item4\n\"";
        String content2 = String.format("Create a roadmap to %s in four title, each with four courses.",prompt);

        ChatMessage message1 = new ChatMessage("system", content1 );
        ChatMessage message2 = new ChatMessage("user", content2 );

        messages.add(message1);
        messages.add(message2);

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder().messages(messages)
                .model("gpt-3.5-turbo").build();

        String gptCompletion = service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();

        List<Sentence> sentences = new ArrayList<>();

        // Parsing gptCompletion string
        String[] lines = gptCompletion.split("\n");
        List<Sentence> formattedSentences = new ArrayList<>();

        for (String line : lines) {
            // Trim any leading or trailing spaces
            line = line.trim();

            // Check if the line contains a dot (.) character
            if (line.contains(".")) {
                // Split the line into ID and content based on the dot
                String[] parts = line.split("\\.", 2); // Split into two parts at the first dot

                // Extract the ID and content from the parts
                String id = parts[0].trim();
                String content = parts[1].trim();

                // Create and add a new Sentence object to the list
                Sentence sentence = new Sentence(id, content);
                formattedSentences.add(sentence);
            }
        }

        return formattedSentences;
    }
}

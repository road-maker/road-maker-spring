package com.roadmaker.v1.gpt.dto;

import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

public class GptRoadmapRequest {

    private List<ChatMessage> chatMessage;

    public List<ChatMessage> getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(List<ChatMessage> chatMessage) {
        this.chatMessage = chatMessage;
    }
}
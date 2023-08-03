package com.roadmaker.gpt.service;

import com.roadmaker.gpt.dto.GptDetailResponse;
import com.roadmaker.gpt.dto.GptRoadmapResponse;
import com.theokanning.openai.completion.chat.ChatMessage;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public interface GptService {

    public String getGptAnswer(String content1, String content2);

    public List<GptRoadmapResponse> messageParsing(String gptResponse);

//    public String makeDetailsAuto (List<GptRoadmapResponse> response);
    public GptDetailResponse makeDetails (String course);
}

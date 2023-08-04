package com.roadmaker.gpt.service;

import com.roadmaker.gpt.dto.NodeDetail;
import com.roadmaker.gpt.dto.RoadmapData;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

@Service
@Transactional
public interface GptService {

    public String getGptAnswer(String content1, String content2);

    public List<RoadmapData> messageParsing(String gptResponse);

    public List<NodeDetail> makeDetailsAuto (List<RoadmapData> response);
    public NodeDetail makeDetails (String course);
    public NodeDetail makeDetails (String course, String id);

}

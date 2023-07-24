package com.roadmaker.boj.controller;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BojController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/api/crawl")
    public boolean checkProb(@RequestParam String bojName, @RequestParam String probNumber) {
        try {
            // Jsoup을 이용하여 해당 웹 페이지에 연결
            Document doc = Jsoup.connect(String.format("https://www.acmicpc.net/user/%s",bojName)).get();

            // 원하는 정보가 있는 특정 클래스(class) 선택 (예시로 "problem-list"라는 클래스를 가정합니다)
            String targetClass = "problem-list"; // 대상 클래스 이름으로 변경해야 합니다.

            // 해당 클래스를 가진 요소 선택
            Element classElement = doc.getElementsByClass(targetClass).first();

            // 해당 클래스의 모든 하위 요소들 선택
            Elements elements = classElement.getElementsByTag("a");

            // 원하는 정보가 들어있는 요소 추출 및 처리
            List<Integer> dataToCompare = new ArrayList<>();
            for (Element element : elements) {
                // 예시로 해당 요소의 텍스트를 가져와서 정수로 변환하여 리스트에 추가합니다.
                String elementText = element.text();
                int value = Integer.parseInt(elementText.trim());
                dataToCompare.add(value);
            }

            // 이분 탐색으로 대조
            int targetValue = Integer.parseInt(probNumber);
            int low = 0;
            int high = dataToCompare.size() - 1;
            while (low <= high) {
                int mid = low + (high - low) / 2;
                int midValue = dataToCompare.get(mid);

                if (midValue == targetValue) {
                    // 찾았으면 true를 반환하고 메서드를 종료합니다.
                    return true;
                } else if (midValue < targetValue) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        } catch (Exception e) {
            logger.debug("Error:", e);
        }
        // 원하는 정보를 찾지 못했을 경우 false를 반환합니다.
        return false;
    }
}
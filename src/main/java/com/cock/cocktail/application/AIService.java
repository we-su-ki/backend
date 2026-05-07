package com.cock.cocktail.application;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    // 파이썬 FastAPI 서버가 실행 중인 주소
    private final String AI_SERVER_URL = "http://localhost:8000/predict";

    public Map<String, Object> getFlavorPrediction(List<Map<String, Object>> ingredients) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> request = new HashMap<>();
        request.put("ingredients", ingredients);

        try {
            // 파이썬 서버에 데이터를 던지고 결과를 받아옴
            return restTemplate.postForObject(AI_SERVER_URL, request, Map.class);
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "AI 서버가 응답하지 않습니다.");
            return errorMap;
        }
    }
}
package com.cock.cocktail.infrastructure;

import com.cock.cocktail.application.TasteProfileTranslator;
import com.cock.cocktail.domain.taste.TasteProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiTasteProfileTranslator implements TasteProfileTranslator {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.gemini.url}")
    private String geminiUrl;

    @Value("${api.gemini.key}")
    private String geminiApiKey;

    private static final String SYSTEM_PROMPT = """
            너는 칵테일 맛 표현을 수치 벡터로 변환하는 분류기다.

            사용자가 입력한 자연어 맛 표현을 아래 12개 축으로 변환하라.

            각 축의 의미:
            - sweetness: 단맛. 설탕, 꿀, 시럽, 리큐르, 디저트, 달콤함.
            - sourness: 신맛. 레몬, 라임, 식초, 상큼함, 새콤함.
            - bitterness: 쓴맛. 비터, 캄파리, 커피, 자몽 껍질, 드라이함.
            - umamiSalty: 감칠맛/짠맛. 소금, 올리브, 토마토, 감칠맛, 세이보리.
            - fruity: 과일향 전반. 베리, 사과, 복숭아, 열대과일 등.
            - citrus: 시트러스향. 레몬, 라임, 오렌지, 자몽.
            - floral: 꽃향. 장미, 라벤더, 엘더플라워, 향수 같은 향.
            - herbal: 허브/풀향. 민트, 바질, 로즈마리, 진, 풀향.
            - spicy: 향신료향. 계피, 생강, 후추, 정향, 매콤함.
            - woodySmoky: 나무/스모키향. 위스키, 오크, 훈연, 피트, 탄 향.
            - body: 바디감/질감. 크리미함, 묵직함, 꾸덕함, 밀도감.
            - fizzy: 청량감. 탄산, 스파클링, 소다, 토닉, 가벼움.

            점수 규칙:
            - 12개 축은 모두 0~10 사이의 실수로 추정한다.
            - 명시적으로 언급된 맛은 강하게 반영한다.
            - 암시된 맛은 약하게 반영한다.
            - 정보가 없으면 0에 가깝게 둔다.
            - 서로 관련된 축은 함께 반영할 수 있다. 예: "레몬처럼 상큼한" → sourness와 citrus 모두 상승.
            - 단순히 "가벼운"은 body를 낮추고 fizzy를 약간 높일 수 있다.
            - "술맛이 강한"은 bitterness 또는 woodySmoky를 높일 수 있다.
            - 사용자의 선호를 과도하게 해석하지 말고, 표현된 맛에만 근거한다.

            출력은 반드시 JSON만 반환하라.
            설명 문장, 마크다운, 코드블록을 출력하지 마라.

            출력 형식:
            {
              "sweetness": number,
              "sourness": number,
              "bitterness": number,
              "umamiSalty": number,
              "fruity": number,
              "citrus": number,
              "floral": number,
              "herbal": number,
              "spicy": number,
              "woodySmoky": number,
              "body": number,
              "fizzy": number
            }

            사용자 입력:
            """;

    @Override
    public TasteProfile translate(String query) {
        var request = createHttpEntity(query);
        var response = restTemplate.postForObject(geminiUrl, request, GeminiResponse.class);
        var tasteProfile = parseToTasteProfile(response);
        log.info("자연어 → 벡터 변환: \"{}\" → {}", query, tasteProfile);
        return tasteProfile;
    }

    private HttpEntity<String> createHttpEntity(String query) {
        try {
            var requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.set("X-goog-api-key", geminiApiKey);

            var requestBody = """
                    {"contents": [{"parts": [{"text": %s}]}]}
                    """.formatted(objectMapper.writeValueAsString(SYSTEM_PROMPT + query));
            return new HttpEntity<>(requestBody, requestHeaders);

        } catch (JsonProcessingException e) {
            throw new IllegalStateException("LLM 요청 Body 생성 실패 : " + query, e);
        }
    }

    private TasteProfile parseToTasteProfile(GeminiResponse response) {
        if (response == null || response.candidates().isEmpty()) return TasteProfile.empty();
        var parts = response.candidates().get(0).content().parts();
        if (parts.isEmpty()) return TasteProfile.empty();
        var json = parts.get(0).text();
        try {
            return objectMapper.readValue(json, ParsedProfile.class).toTasteProfile();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("LLM 응답을 TasteProfile로 변환 실패: " + json, e);
        }
    }

    private record GeminiResponse(List<Candidate> candidates) {
        private record Candidate(Content content) {
            private record Content(List<Part> parts) {
                private record Part(String text) {}
            }
        }
    }

    private record ParsedProfile(
            double sweetness,
            double sourness,
            double bitterness,
            double umamiSalty,
            double fruity,
            double citrus,
            double floral,
            double herbal,
            double spicy,
            double woodySmoky,
            double body,
            double fizzy
    ) {
        TasteProfile toTasteProfile() {
            return TasteProfile.builder()
                    .sweetness(sweetness)
                    .sourness(sourness)
                    .bitterness(bitterness)
                    .umamiSalty(umamiSalty)
                    .fruity(fruity)
                    .citrus(citrus)
                    .floral(floral)
                    .herbal(herbal)
                    .spicy(spicy)
                    .woodySmoky(woodySmoky)
                    .body(body)
                    .fizzy(fizzy)
                    .build();
        }
    }
}

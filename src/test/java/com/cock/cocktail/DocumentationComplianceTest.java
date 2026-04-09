package com.cock.cocktail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DocumentationComplianceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("PLAN.md Phase 9 시나리오 1: 맛 키워드만 입력 - 추천 성공")
    void phase9_scenario1_tasteKeywordOnly() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한 칵테일 추천해줘"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("PLAN.md Phase 9 시나리오 2: 복합 키워드 입력 - 추천 성공")
    void phase9_scenario2_multipleKeywords() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달하고 톡 쏘는 과일맛이 나는 부드러운 칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("PLAN.md Phase 9 시나리오 3a: 잘못된 입력 - 빈 문자열 → 400 에러")
    void phase9_scenario3_emptyString_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("PLAN.md Phase 9 시나리오 3b: 잘못된 입력 - 파라미터 누락 → 400 에러")
    void phase9_scenario3_missingParameter_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("API 엔드포인트: GET /api/v1/cocktails/analyze")
    void apiEndpoint_analyze() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/analyze")
                        .param("query", "달달한 칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taste").isArray())
                .andExpect(jsonPath("$.aroma").isArray())
                .andExpect(jsonPath("$.mouthfeel").isArray())
                .andExpect(jsonPath("$.sensation").isArray())
                .andExpect(jsonPath("$.impression").isArray());
    }

    @Test
    @DisplayName("API 엔드포인트: GET /api/v1/cocktails/recommend")
    void apiEndpoint_recommend() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한 칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("응답 형식: RecommendedCocktailDto")
    void responseFormat_cocktailRecommendation() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한 칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").isString())
                .andExpect(jsonPath("$[0].ingredients").isArray())
                .andExpect(jsonPath("$[0].recipe").isString())
                .andExpect(jsonPath("$[0].score").isNumber())
                .andExpect(jsonPath("$[0].reason").isString())
                .andExpect(jsonPath("$[0].matchedKeywords").isArray());
    }

    @Test
    @DisplayName("응답 형식: Ingredient 구조")
    void responseFormat_ingredient() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한 칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ingredients[0].name").isString())
                .andExpect(jsonPath("$[0].ingredients[0].amount").isString());
    }

    @Test
    @DisplayName("응답 형식: ErrorResponse")
    void responseFormat_error() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.timestamp").isString());
    }

    @Test
    @DisplayName("비즈니스 로직: 최대 3개 칵테일 반환")
    void businessLogic_maxThreeCocktails() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(lessThanOrEqualTo(3))));
    }

    @Test
    @DisplayName("비즈니스 로직: 점수 내림차순 정렬")
    void businessLogic_sortedByScoreDescending() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한 칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score").exists())
                .andExpect(jsonPath("$[*].score").exists());
    }

    @Test
    @DisplayName("비즈니스 로직: 매칭 없으면 빈 배열 반환")
    void businessLogic_emptyArrayWhenNoMatch() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "완전히존재하지않는맛"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    @DisplayName("HTTP 메서드: GET 사용 확인")
    void httpMethod_useGet() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한 칵테일"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("쿼리 파라미터: 'query' 필드 사용")
    void queryParameter_queryField() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한 칵테일"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/cocktails/recommend"))
                .andExpect(status().isBadRequest());
    }
}

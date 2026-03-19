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

/**
 * 칵테일 추천 E2E 통합 테스트
 * <p>
 * KeywordAnalyzer → CocktailMatcher → Service → Controller
 * 전체 플로우 검증
 */
@SpringBootTest
@AutoConfigureMockMvc
class CocktailRecommendationE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("시나리오 1: 맛 키워드만 입력 - 추천 성공")
    void shouldRecommendWithTasteKeyword() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한 칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.cocktails[*].id").exists())
                .andExpect(jsonPath("$.cocktails[*].name").exists())
                .andExpect(jsonPath("$.cocktails[*].score").exists())
                .andExpect(jsonPath("$.cocktails[*].reason").exists())
                .andExpect(jsonPath("$.cocktails[*].matchedKeywords").exists());
    }

    @Test
    @DisplayName("시나리오 2: 복합 키워드 입력 - 추천 성공")
    void shouldRecommendWithMultipleKeywords() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달하고 과일향 나는 부드러운 칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray())
                .andExpect(jsonPath("$.count").isNumber());
    }

    @Test
    @DisplayName("시나리오 3: 키워드 없는 입력 - 빈 리스트 반환")
    void shouldReturnEmptyListForNonKeywordQuery() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "칵테일 추천해줘"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails", empty()))
                .andExpect(jsonPath("$.count", is(0)));
    }

    @Test
    @DisplayName("시나리오 4: 존재하지 않는 키워드 - 빈 리스트 반환")
    void shouldReturnEmptyListForNonexistentKeyword() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "완전히존재하지않는맛"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails", empty()))
                .andExpect(jsonPath("$.count", is(0)));
    }

    @Test
    @DisplayName("시나리오 5: 전체 플로우 - 점수, 이유, 매칭 키워드 포함 확인")
    void shouldIncludeAllResponseFields() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한 과일향 칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray())
                .andExpect(jsonPath("$.count").exists())
                .andExpect(jsonPath("$.cocktails[?(@.score > 0)]").exists())
                .andExpect(jsonPath("$.cocktails[?(@.reason)]").exists())
                .andExpect(jsonPath("$.cocktails[?(@.matchedKeywords)]").exists());
    }

    @Test
    @DisplayName("시나리오 6: 추천 결과 최대 3개 제한")
    void shouldReturnMaximumThreeCocktails() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails", hasSize(lessThanOrEqualTo(3))));
    }

    @Test
    @DisplayName("시나리오 7: 재료 및 레시피 정보 포함 확인")
    void shouldIncludeIngredientsAndRecipe() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한 칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails[*].ingredients").exists())
                .andExpect(jsonPath("$.cocktails[*].recipe").exists());
    }
}

package com.cock.cocktail.web;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.Ingredient;
import com.cock.cocktail.domain.MatchedCocktail;
import com.cock.cocktail.domain.SensoryAxis;
import com.cock.cocktail.domain.SensoryDescriptors;
import com.cock.cocktail.repository.CocktailRepository;
import com.cock.cocktail.service.CocktailRecommendationService;
import com.cock.cocktail.service.KeywordAnalyzer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({CocktailController.class, com.cock.cocktail.exception.GlobalExceptionHandler.class})
class CocktailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CocktailRepository cocktailRepository;

    @MockitoBean
    private KeywordAnalyzer keywordAnalyzer;

    @MockitoBean
    private CocktailRecommendationService recommendationService;

    @Test
    @DisplayName("GET /api/v1/cocktails/analyze - 정상 요청")
    void shouldAnalyzeQuery() throws Exception {
        var query = "달달하고 과일향 나는 칵테일";
        var sensoryDescriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .build();
        when(keywordAnalyzer.analyze(query)).thenReturn(sensoryDescriptors);

        mockMvc.perform(get("/api/v1/cocktails/analyze")
                        .param("query", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taste", hasSize(1)))
                .andExpect(jsonPath("$.taste[0]", is("sweet")))
                .andExpect(jsonPath("$.aroma", hasSize(1)))
                .andExpect(jsonPath("$.aroma[0]", is("fruity")))
                .andExpect(jsonPath("$.mouthfeel", empty()))
                .andExpect(jsonPath("$.sensation", empty()))
                .andExpect(jsonPath("$.impression", empty()));
    }

    @Test
    @DisplayName("GET /api/v1/cocktails/analyze - 매칭되는 키워드 없음")
    void shouldReturnEmptyDescriptorsWhenNoMatch() throws Exception {
        var query = "칵테일 추천해줘";
        var sensoryDescriptors = SensoryDescriptors.builder().build();
        when(keywordAnalyzer.analyze(query)).thenReturn(sensoryDescriptors);

        mockMvc.perform(get("/api/v1/cocktails/analyze")
                        .param("query", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taste", empty()))
                .andExpect(jsonPath("$.aroma", empty()))
                .andExpect(jsonPath("$.mouthfeel", empty()))
                .andExpect(jsonPath("$.sensation", empty()))
                .andExpect(jsonPath("$.impression", empty()));
    }

    @Test
    @DisplayName("GET /api/v1/cocktails/analyze - 복수 descriptor 추출")
    void shouldReturnMultipleDescriptors() throws Exception {
        var query = "달달하고 새콤한 과일향 칵테일";
        var sensoryDescriptors = SensoryDescriptors.builder()
                .taste(Set.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.TASTE, "sour")
                ))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .build();
        when(keywordAnalyzer.analyze(query)).thenReturn(sensoryDescriptors);

        mockMvc.perform(get("/api/v1/cocktails/analyze")
                        .param("query", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taste", hasSize(2)))
                .andExpect(jsonPath("$.taste", containsInAnyOrder("sweet", "sour")))
                .andExpect(jsonPath("$.aroma", hasSize(1)))
                .andExpect(jsonPath("$.aroma[0]", is("fruity")));
    }

    @Test
    @DisplayName("GET /api/v1/cocktails/analyze - 전체 축 매칭")
    void shouldReturnAllAxes() throws Exception {
        var query = "달달하고 과일향 나는 부드럽고 톡 쏘는 여름 칵테일";
        var sensoryDescriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .mouthfeel(Set.of(new DescriptorCode(SensoryAxis.MOUTHFEEL, "smooth")))
                .sensation(Set.of(new DescriptorCode(SensoryAxis.SENSATION, "carbonated")))
                .impression(Set.of(new DescriptorCode(SensoryAxis.IMPRESSION, "summer")))
                .build();
        when(keywordAnalyzer.analyze(query)).thenReturn(sensoryDescriptors);

        mockMvc.perform(get("/api/v1/cocktails/analyze")
                        .param("query", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taste", hasSize(1)))
                .andExpect(jsonPath("$.aroma", hasSize(1)))
                .andExpect(jsonPath("$.mouthfeel", hasSize(1)))
                .andExpect(jsonPath("$.sensation", hasSize(1)))
                .andExpect(jsonPath("$.impression", hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/v1/cocktails/analyze - query 파라미터 누락")
    void shouldReturnBadRequestWhenQueryIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/analyze"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/cocktails/recommend - 정상 요청")
    void shouldRecommendCocktails() throws Exception {
        var cocktail = Cocktail.builder()
                .id(1L)
                .name("Mojito")
                .ingredients(List.of(new Ingredient("럼", "50ml")))
                .recipe("Recipe")
                .sensoryDescriptors(List.of())
                .build();

        var matchedCocktail = new MatchedCocktail(
                cocktail,
                0.85,
                "달달한 특징을 가진 칵테일입니다.",
                Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet"))
        );

        when(recommendationService.recommend("달달한 칵테일")).thenReturn(List.of(matchedCocktail));

        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한 칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Mojito")))
                .andExpect(jsonPath("$[0].score", is(0.85)))
                .andExpect(jsonPath("$[0].reason", is("달달한 특징을 가진 칵테일입니다.")))
                .andExpect(jsonPath("$[0].matchedKeywords", hasSize(1)))
                .andExpect(jsonPath("$[0].matchedKeywords[0]", is("sweet")));
    }

    @Test
    @DisplayName("GET /api/v1/cocktails/recommend - 빈 결과")
    void shouldReturnEmptyRecommendations() throws Exception {
        when(recommendationService.recommend("칵테일 추천해줘")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "칵테일 추천해줘"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    @DisplayName("GET /api/v1/cocktails/recommend - query 파라미터 누락")
    void shouldReturnBadRequestWhenQueryParameterMissing() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/cocktails/recommend - query 빈 문자열")
    void shouldReturnBadRequestWhenQueryIsBlank() throws Exception {
        when(recommendationService.recommend("")).thenThrow(new IllegalArgumentException("query must not be blank"));

        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", ""))
                .andExpect(status().isBadRequest());
    }
}

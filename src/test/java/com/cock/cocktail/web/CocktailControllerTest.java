package com.cock.cocktail.web;

import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.SensoryAxis;
import com.cock.cocktail.domain.SensoryDescriptors;
import com.cock.cocktail.service.KeywordAnalyzer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CocktailController.class)
class CocktailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KeywordAnalyzer keywordAnalyzer;

    @Test
    @DisplayName("GET /api/v1/cocktails/analyze - 정상 요청")
    void shouldAnalyzeQuery() throws Exception {
        // given
        var query = "달달하고 과일향 나는 칵테일";
        var sensoryDescriptors = new SensoryDescriptors(Map.of(
                SensoryAxis.TASTE, Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")),
                SensoryAxis.AROMA, Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity"))
        ));
        when(keywordAnalyzer.analyze(query)).thenReturn(sensoryDescriptors);

        // when & then
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
        // given
        var query = "칵테일 추천해줘";
        var sensoryDescriptors = new SensoryDescriptors(Map.of());
        when(keywordAnalyzer.analyze(query)).thenReturn(sensoryDescriptors);

        // when & then
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
        // given
        var query = "달달하고 새콤한 과일향 칵테일";
        var sensoryDescriptors = new SensoryDescriptors(Map.of(
                SensoryAxis.TASTE, Set.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.TASTE, "sour")
                ),
                SensoryAxis.AROMA, Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity"))
        ));
        when(keywordAnalyzer.analyze(query)).thenReturn(sensoryDescriptors);

        // when & then
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
        // given
        var query = "달달하고 과일향 나는 부드럽고 톡 쏘는 여름 칵테일";
        var sensoryDescriptors = new SensoryDescriptors(Map.of(
                SensoryAxis.TASTE, Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")),
                SensoryAxis.AROMA, Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")),
                SensoryAxis.MOUTHFEEL, Set.of(new DescriptorCode(SensoryAxis.MOUTHFEEL, "smooth")),
                SensoryAxis.SENSATION, Set.of(new DescriptorCode(SensoryAxis.SENSATION, "carbonated")),
                SensoryAxis.IMPRESSION, Set.of(new DescriptorCode(SensoryAxis.IMPRESSION, "summer"))
        ));
        when(keywordAnalyzer.analyze(query)).thenReturn(sensoryDescriptors);

        // when & then
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
        // when & then
        mockMvc.perform(get("/api/v1/cocktails/analyze"))
                .andExpect(status().isBadRequest());
    }
}

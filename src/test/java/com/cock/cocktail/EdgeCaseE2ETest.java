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
class EdgeCaseE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("엣지 케이스: 매우 긴 쿼리 (1000자 이상)")
    void shouldHandleVeryLongQuery() throws Exception {
        String longQuery = "달달한 ".repeat(200);

        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", longQuery))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray());
    }

    @Test
    @DisplayName("엣지 케이스: 특수 문자 포함")
    void shouldHandleSpecialCharacters() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한!@#$%^&*()칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray());
    }

    @Test
    @DisplayName("엣지 케이스: 한글, 영어, 숫자 혼합")
    void shouldHandleMixedLanguages() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한sweet123칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray());
    }

    @Test
    @DisplayName("엣지 케이스: 존재하지 않는 키워드만 입력")
    void shouldHandleNonexistentKeywords() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "완전히존재하지않는맛"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails", empty()))
                .andExpect(jsonPath("$.count", is(0)));
    }

    @Test
    @DisplayName("엣지 케이스: 모든 칵테일에 매칭되는 키워드")
    void shouldHandleCommonKeyword() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails", hasSize(lessThanOrEqualTo(3))));
    }

    @Test
    @DisplayName("엣지 케이스: 대소문자 혼합")
    void shouldHandleMixedCase() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "DaLDaLHan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray());
    }

    @Test
    @DisplayName("엣지 케이스: 공백만 포함")
    void shouldHandleWhitespaceOnly() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "   "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("query must not be blank")));
    }

    @Test
    @DisplayName("엣지 케이스: 앞뒤 공백")
    void shouldTrimWhitespace() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "  달달한  "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray());
    }

    @Test
    @DisplayName("엣지 케이스: 반복된 키워드")
    void shouldHandleRepeatedKeywords() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한 달달한 달달한"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray());
    }

    @Test
    @DisplayName("엣지 케이스: 줄바꿈 문자 포함")
    void shouldHandleNewlineCharacters() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한\n칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray());
    }

    @Test
    @DisplayName("엣지 케이스: 탭 문자 포함")
    void shouldHandleTabCharacters() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한\t칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray());
    }

    @Test
    @DisplayName("엣지 케이스: 이모지 포함")
    void shouldHandleEmojis() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한 🍹 칵테일"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray());
    }

    @Test
    @DisplayName("엣지 케이스: 단일 문자")
    void shouldHandleSingleCharacter() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray());
    }

    @Test
    @DisplayName("엣지 케이스: 숫자만")
    void shouldHandleNumbersOnly() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray());
    }

    @Test
    @DisplayName("엣지 케이스: URL 인코딩 특수 문자")
    void shouldHandleUrlEncodedSpecialChars() throws Exception {
        mockMvc.perform(get("/api/v1/cocktails/recommend")
                        .param("query", "달달한&sweet=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cocktails").isArray());
    }
}

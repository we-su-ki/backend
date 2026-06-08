package com.cock.cocktail.web;

import com.cock.cocktail.application.CocktailMatchStrategy;
import com.cock.cocktail.application.TasteProfileTranslator;
import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.taste.TasteMatch;
import com.cock.cocktail.domain.taste.TasteProfile;
import com.cock.cocktail.repository.CocktailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({CocktailController.class, GlobalExceptionHandler.class})
class CocktailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CocktailRepository cocktailRepository;

    @MockitoBean
    private CocktailMatchStrategy matchStrategy;

    @MockitoBean
    private TasteProfileTranslator tasteProfileTranslator;

    @Test
    @DisplayName("GET /cocktails - 기본 파라미터로 페이지네이션 응답 반환")
    void shouldReturnPagedCocktailList() throws Exception {
        var cocktails = List.of(
                Cocktail.builder()
                        .id(1L)
                        .name("Mojito")
                        .glassRaw("Highball")
                        .garnishRaw("Mint")
                        .methodCategory("Muddle")
                        .isAlcohol(true)
                        .pureAlcoholGrams(14.0)
                        .proofInsideBracket(24.0)
                        .scoreStrength(3.0)
                        .scoreSweetSour(2.0)
                        .reviewText("상큼한 민트 칵테일")
                        .build(),
                Cocktail.builder().id(2L).name("Margarita").build()
        );
        when(cocktailRepository.findAll(any())).thenReturn(new PageImpl<>(cocktails, PageRequest.of(0, 20), 2));

        mockMvc.perform(get("/cocktails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(20)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("Mojito")))
                .andExpect(jsonPath("$.content[0].glassRaw", is("Highball")))
                .andExpect(jsonPath("$.content[0].garnishRaw", is("Mint")))
                .andExpect(jsonPath("$.content[0].methodCategory", is("Muddle")))
                .andExpect(jsonPath("$.content[0].isAlcohol", is(true)))
                .andExpect(jsonPath("$.content[0].pureAlcoholGrams", is(14.0)))
                .andExpect(jsonPath("$.content[0].proofInsideBracket", is(24.0)))
                .andExpect(jsonPath("$.content[0].scoreStrength", is(3.0)))
                .andExpect(jsonPath("$.content[0].scoreSweetSour", is(2.0)))
                .andExpect(jsonPath("$.content[0].reviewText", is("상큼한 민트 칵테일")))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].name", is("Margarita")));
    }

    @Test
    @DisplayName("GET /cocktails - 빈 목록")
    void shouldReturnEmptyCocktailList() throws Exception {
        when(cocktailRepository.findAll(any())).thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 20), 0));

        mockMvc.perform(get("/cocktails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", empty()))
                .andExpect(jsonPath("$.totalElements", is(0)));
    }

    @Test
    @DisplayName("GET /cocktails - 음수 page/size는 0/20으로 방어")
    void shouldSanitizeNegativePageAndSize() throws Exception {
        when(cocktailRepository.findAll(eq(PageRequest.of(0, 20)))).thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 20), 0));

        mockMvc.perform(get("/cocktails").param("page", "-1").param("size", "-5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(20)));
    }

    @Test
    @DisplayName("GET /cocktails - method 파라미터로 필터링")
    void shouldFilterByMethod() throws Exception {
        var shakeCocktail = Cocktail.builder().id(1L).name("Daiquiri").methodCategory("Shake").build();
        when(cocktailRepository.findAllByMethodCategoryIgnoreCase(eq("shake"), any()))
                .thenReturn(new PageImpl<>(List.of(shakeCocktail), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/cocktails").param("method", "shake"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Daiquiri")));
    }

    @Test
    @DisplayName("GET /cocktails - method 파라미터 대소문자 구분 없음")
    void shouldFilterByMethodCaseInsensitive() throws Exception {
        var shakeCocktail = Cocktail.builder().id(1L).name("Daiquiri").methodCategory("Shake").build();
        when(cocktailRepository.findAllByMethodCategoryIgnoreCase(eq("SHAKE"), any()))
                .thenReturn(new PageImpl<>(List.of(shakeCocktail), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/cocktails").param("method", "SHAKE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("POST /cocktails/recommend - 자연어 쿼리로 칵테일 추천")
    void shouldReturnRecommendedCocktailsFromNaturalLanguage() throws Exception {
        var cocktail = Cocktail.builder()
                .id(1L)
                .name("Mojito")
                .isAlcohol(true)
                .scoreStrength(2.0)
                .scoreSweetSour(3.0)
                .build();
        var tasteProfile = TasteProfile.builder().sourness(7.5).fizzy(8.5).build();
        when(tasteProfileTranslator.translate(eq("시원하고 상큼한 칵테일")))
                .thenReturn(tasteProfile);
        when(matchStrategy.match(tasteProfile, null))
                .thenReturn(List.of(new TasteMatch(cocktail, 0.88)));

        mockMvc.perform(post("/cocktails/recommend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\": \"시원하고 상큼한 칵테일\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Mojito")))
                .andExpect(jsonPath("$[0].isAlcohol", is(true)))
                .andExpect(jsonPath("$[0].scoreStrength", is(2.0)))
                .andExpect(jsonPath("$[0].scoreSweetSour", is(3.0)))
                .andExpect(jsonPath("$[0].matchScore", is(0.88)));
    }
}

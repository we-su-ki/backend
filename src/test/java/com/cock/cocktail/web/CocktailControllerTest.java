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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.empty;
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
    @DisplayName("GET /cocktails - 칵테일 목록 반환")
    void shouldReturnCocktailList() throws Exception {
        when(cocktailRepository.findAll()).thenReturn(List.of(
                Cocktail.builder().name("Mojito").imageUrl("https://example.com/mojito.jpg").build(),
                Cocktail.builder().name("Margarita").build()
        ));

        mockMvc.perform(get("/cocktails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Mojito")))
                .andExpect(jsonPath("$[0].imageUrl", is("https://example.com/mojito.jpg")))
                .andExpect(jsonPath("$[1].name", is("Margarita")));
    }

    @Test
    @DisplayName("GET /cocktails - 빈 목록")
    void shouldReturnEmptyCocktailList() throws Exception {
        when(cocktailRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/cocktails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    @DisplayName("POST /cocktails/recommend - 자연어 쿼리로 칵테일 추천")
    void shouldReturnRecommendedCocktailsFromNaturalLanguage() throws Exception {
        var cocktail = Cocktail.builder().name("Mojito").build();
        var tasteProfile = TasteProfile.builder().sourness(7.5).fizzy(8.5).build();
        when(tasteProfileTranslator.translate(eq("시원하고 상큼한 칵테일")))
                .thenReturn(tasteProfile);
        when(matchStrategy.match(tasteProfile))
                .thenReturn(List.of(new TasteMatch(cocktail, 0.88)));

        mockMvc.perform(post("/cocktails/recommend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\": \"시원하고 상큼한 칵테일\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Mojito")))
                .andExpect(jsonPath("$[0].matchScore", is(0.88)));
    }
}

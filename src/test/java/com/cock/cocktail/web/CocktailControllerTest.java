package com.cock.cocktail.web;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.CocktailIngredient;
import com.cock.cocktail.domain.Ingredient;
import com.cock.cocktail.domain.taste.FlavorVector;
import com.cock.cocktail.domain.taste.TasteMatch;
import com.cock.cocktail.domain.taste.TasteQuery;
import com.cock.cocktail.repository.CocktailRepository;
import com.cock.cocktail.service.cocktail.CocktailMatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
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
    private CocktailMatchService matchService;

    @Test
    @DisplayName("GET /api/v1/cocktails/match - 정상 요청")
    void shouldMatchCocktailsByTasteQuery() throws Exception {
        var cocktail = Cocktail.builder()
                .id(1L)
                .name("Mojito")
                .cocktailIngredients(List.of(new CocktailIngredient(null, new Ingredient("럼"), 50)))
                .recipe("Recipe")
                .flavorVector(FlavorVector.builder().sweet(0.9).build())
                .build();

        when(matchService.match(new TasteQuery(4.0, null, null, null, null, null)))
                .thenReturn(List.of(new TasteMatch(cocktail, 0.92)));

        mockMvc.perform(get("/api/v1/cocktails/match")
                        .param("sweet", "4.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Mojito")))
                .andExpect(jsonPath("$[0].score", is(0.92)))
                .andExpect(jsonPath("$[0].recipe", is("Recipe")));
    }

    @Test
    @DisplayName("GET /api/v1/cocktails/match - 파라미터 없으면 빈 결과")
    void shouldReturnEmptyWhenNoQueryParams() throws Exception {
        when(matchService.match(new TasteQuery(null, null, null, null, null, null)))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/cocktails/match"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }
}

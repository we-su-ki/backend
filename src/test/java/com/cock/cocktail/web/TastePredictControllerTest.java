package com.cock.cocktail.web;

import com.cock.cocktail.domain.taste.FlavorVector;
import com.cock.cocktail.service.FlavorVectorResolver;
import com.cock.cocktail.domain.IngredientAmount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({TastePredictController.class, com.cock.cocktail.exception.GlobalExceptionHandler.class})
class TastePredictControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlavorVectorResolver flavorVectorResolver;

    @Test
    @DisplayName("POST /api/v1/predict - TasteProfile 반환")
    void shouldReturnTasteProfile() throws Exception {
        var ingredients = List.of(new IngredientAmount(1L, 50), new IngredientAmount(2L, 20));
        var flavorVector = FlavorVector.builder().sweet(5.0).sour(3.0).build();
        when(flavorVectorResolver.resolve(ingredients)).thenReturn(flavorVector);

        mockMvc.perform(post("/api/v1/predict")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"ingredients":[{"id":1,"amount":50},{"id":2,"amount":20}]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sweet").isNumber())
                .andExpect(jsonPath("$.body").isNumber())
                .andExpect(jsonPath("$.bitter").isNumber())
                .andExpect(jsonPath("$.abv").isNumber())
                .andExpect(jsonPath("$.smoky").isNumber())
                .andExpect(jsonPath("$.sour").isNumber());
    }

    @Test
    @DisplayName("POST /api/v1/predict - 빈 재료 목록")
    void shouldHandleEmptyIngredients() throws Exception {
        when(flavorVectorResolver.resolve(List.of())).thenReturn(new FlavorVector());

        mockMvc.perform(post("/api/v1/predict")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"ingredients":[]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sweet", is(0.0)))
                .andExpect(jsonPath("$.body", is(0.0)));
    }
}

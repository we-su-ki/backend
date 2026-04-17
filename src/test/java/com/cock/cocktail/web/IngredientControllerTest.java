package com.cock.cocktail.web;

import com.cock.cocktail.domain.Ingredient;
import com.cock.cocktail.domain.IngredientAmount;
import com.cock.cocktail.domain.taste.FlavorVector;
import com.cock.cocktail.repository.IngredientRepository;
import com.cock.cocktail.application.FlavorVectorResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({IngredientController.class, GlobalExceptionHandler.class})
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngredientRepository ingredientRepository;

    @MockitoBean
    private FlavorVectorResolver flavorVectorResolver;

    @Test
    @DisplayName("GET /ingredients - 재료 목록 반환")
    void shouldReturnIngredientList() throws Exception {
        var rum = new Ingredient("럼");
        ReflectionTestUtils.setField(rum, "id", 1L);
        var limeJuice = new Ingredient("라임 주스");
        ReflectionTestUtils.setField(limeJuice, "id", 2L);

        when(ingredientRepository.findAll()).thenReturn(List.of(
                rum,
                limeJuice
        ));

        mockMvc.perform(get("/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("럼")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("라임 주스")));
    }

    @Test
    @DisplayName("GET /ingredients - 빈 목록")
    void shouldReturnEmptyList() throws Exception {
        when(ingredientRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    @DisplayName("POST /ingredients/predict - TasteProfile 반환")
    void shouldReturnTasteProfile() throws Exception {
        var ingredients = List.of(new IngredientAmount(1L, 50), new IngredientAmount(2L, 20));
        when(flavorVectorResolver.resolve(ingredients)).thenReturn(FlavorVector.builder().sweet(5.0).sour(3.0).build());

        mockMvc.perform(post("/ingredients/predict")
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
    @DisplayName("POST /ingredients/predict - 빈 재료 목록")
    void shouldHandleEmptyIngredients() throws Exception {
        when(flavorVectorResolver.resolve(List.of())).thenReturn(new FlavorVector());

        mockMvc.perform(post("/ingredients/predict")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"ingredients":[]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sweet", is(0.0)))
                .andExpect(jsonPath("$.body", is(0.0)));
    }
}

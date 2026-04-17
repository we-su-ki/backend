package com.cock.cocktail.web;

import com.cock.cocktail.domain.Ingredient;
import com.cock.cocktail.repository.IngredientRepository;
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

@WebMvcTest({IngredientController.class, com.cock.cocktail.exception.GlobalExceptionHandler.class})
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngredientRepository ingredientRepository;

    @Test
    @DisplayName("GET /api/v1/ingredients - 재료 목록 반환")
    void shouldReturnIngredientList() throws Exception {
        when(ingredientRepository.findAll()).thenReturn(List.of(
                new Ingredient("럼"),
                new Ingredient("라임 주스")
        ));

        mockMvc.perform(get("/api/v1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("럼")))
                .andExpect(jsonPath("$[1].name", is("라임 주스")));
    }

    @Test
    @DisplayName("GET /api/v1/ingredients - 빈 목록")
    void shouldReturnEmptyList() throws Exception {
        when(ingredientRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }
}

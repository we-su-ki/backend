package com.cock.cocktail.web;

import com.cock.cocktail.application.IngredientAmount;
import com.cock.cocktail.application.TasteProfilePrediction;
import com.cock.cocktail.application.TasteProfilePredictor;
import com.cock.cocktail.domain.Ingredient;
import com.cock.cocktail.domain.MethodCategory;
import com.cock.cocktail.domain.taste.TasteProfile;
import com.cock.cocktail.repository.IngredientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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
    private TasteProfilePredictor tasteProfilePredictor;

    @Test
    @DisplayName("GET /ingredients - 재료 목록 반환")
    void shouldReturnIngredientList() throws Exception {
        when(ingredientRepository.findAll()).thenReturn(List.of(
                Ingredient.builder()
                        .id(1L)
                        .name("Rum")
                        .category("Spirit")
                        .role("Base")
                        .isAlcohol(true)
                        .tier(1)
                        .frequency(120L)
                        .build(),
                Ingredient.builder().id(2L).name("Lime juice").isAlcohol(false).build()
        ));

        mockMvc.perform(get("/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Rum")))
                .andExpect(jsonPath("$[0].category", is("Spirit")))
                .andExpect(jsonPath("$[0].role", is("Base")))
                .andExpect(jsonPath("$[0].isAlcohol", is(true)))
                .andExpect(jsonPath("$[0].tier", is(1)))
                .andExpect(jsonPath("$[0].frequency", is(120)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Lime juice")))
                .andExpect(jsonPath("$[1].isAlcohol", is(false)));
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
        when(tasteProfilePredictor.predict(ingredients, MethodCategory.Shake))
                .thenReturn(new TasteProfilePrediction(TasteProfile.builder().sweetness(5.0).sourness(3.0).build(), 14.2));

        mockMvc.perform(post("/ingredients/predict")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"ingredients":[{"id":1,"amount":50},{"id":2,"amount":20}],"methodCategory":"Shake"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.abv").isNumber())
                .andExpect(jsonPath("$.sweetness").isNumber())
                .andExpect(jsonPath("$.sourness").isNumber())
                .andExpect(jsonPath("$.bitterness").isNumber())
                .andExpect(jsonPath("$.umamiSalty").isNumber())
                .andExpect(jsonPath("$.fruity").isNumber())
                .andExpect(jsonPath("$.citrus").isNumber())
                .andExpect(jsonPath("$.floral").isNumber())
                .andExpect(jsonPath("$.herbal").isNumber())
                .andExpect(jsonPath("$.spicy").isNumber())
                .andExpect(jsonPath("$.woodySmoky").isNumber())
                .andExpect(jsonPath("$.body").isNumber())
                .andExpect(jsonPath("$.fizzy").isNumber());
    }

    @Test
    @DisplayName("POST /ingredients/predict - 빈 재료 목록")
    void shouldHandleEmptyIngredients() throws Exception {
        when(tasteProfilePredictor.predict(List.of(), MethodCategory.NONE))
                .thenReturn(new TasteProfilePrediction(TasteProfile.empty(), 0.0));

        mockMvc.perform(post("/ingredients/predict")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"ingredients":[]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.abv", is(0.0)))
                .andExpect(jsonPath("$.sweetness", is(0.0)))
                .andExpect(jsonPath("$.body", is(0.0)));
    }
}

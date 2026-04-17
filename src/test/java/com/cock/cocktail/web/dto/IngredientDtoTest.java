package com.cock.cocktail.web.dto;

import com.cock.cocktail.ingredient.Ingredient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IngredientDtoTest {

    @Test
    @DisplayName("Ingredient → IngredientDto 변환")
    void shouldConvertFromIngredient() {
        var ingredient = new Ingredient("럼");

        var dto = IngredientDto.from(ingredient);

        assertThat(dto.name()).isEqualTo("럼");
    }

    @Test
    @DisplayName("빈 이름 Ingredient 변환")
    void shouldConvertIngredientWithEmptyName() {
        var ingredient = new Ingredient("");

        var dto = IngredientDto.from(ingredient);

        assertThat(dto.name()).isEmpty();
    }
}

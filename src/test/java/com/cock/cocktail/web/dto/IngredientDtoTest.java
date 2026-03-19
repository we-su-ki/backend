package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.Ingredient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class IngredientDtoTest {

    @Test
    @DisplayName("Ingredient → IngredientDto 변환")
    void shouldConvertFromIngredient() {
        var ingredient = new Ingredient("럼", "50ml");

        var dto = IngredientDto.from(ingredient);

        assertAll(
                () -> assertThat(dto.name()).isEqualTo("럼"),
                () -> assertThat(dto.amount()).isEqualTo("50ml")
        );
    }

    @Test
    @DisplayName("빈 값 Ingredient 변환")
    void shouldConvertIngredientWithEmptyValues() {
        var ingredient = new Ingredient("", "");

        var dto = IngredientDto.from(ingredient);

        assertAll(
                () -> assertThat(dto.name()).isEmpty(),
                () -> assertThat(dto.amount()).isEmpty()
        );
    }
}

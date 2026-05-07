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
        var ingredient = Ingredient.builder()
                .id(1L)
                .name("Rum")
                .build();

        var dto = IngredientDto.from(ingredient);

        assertAll(
                () -> assertThat(dto.id()).isEqualTo(1L),
                () -> assertThat(dto.name()).isEqualTo("Rum")
        );
    }

    @Test
    @DisplayName("name이 null인 Ingredient 변환")
    void shouldConvertIngredientWithNullName() {
        var ingredient = Ingredient.builder()
                .id(2L)
                .build();

        var dto = IngredientDto.from(ingredient);

        assertAll(
                () -> assertThat(dto.id()).isEqualTo(2L),
                () -> assertThat(dto.name()).isNull()
        );
    }
}

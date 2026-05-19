package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.Ingredient;
import com.cock.cocktail.domain.taste.TasteProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class IngredientDtoTest {

    @Test
    @DisplayName("Ingredient → IngredientDto 변환")
    void shouldConvertFromIngredient() {
        var ingredient = Ingredient.builder()
                .id(1L)
                .name("Rum")
                .category("Spirit")
                .role("Base")
                .isAlcohol(true)
                .tier(1L)
                .frequency(120L)
                .flavorTags(List.of("sweet", "woody"))
                .tasteProfile(TasteProfile.builder().sweetness(3.0).woodySmoky(6.0).build())
                .build();

        var dto = IngredientDto.from(ingredient);

        assertAll(
                () -> assertThat(dto.id()).isEqualTo(1L),
                () -> assertThat(dto.name()).isEqualTo("Rum"),
                () -> assertThat(dto.category()).isEqualTo("Spirit"),
                () -> assertThat(dto.role()).isEqualTo("Base"),
                () -> assertThat(dto.isAlcohol()).isTrue(),
                () -> assertThat(dto.tier()).isEqualTo(1L),
                () -> assertThat(dto.frequency()).isEqualTo(120L),
                () -> assertThat(dto.flavorTags()).containsExactly("sweet", "woody"),
                () -> assertThat(dto.tasteProfile().sweetness()).isEqualTo(3.0),
                () -> assertThat(dto.tasteProfile().woodySmoky()).isEqualTo(6.0)
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
                () -> assertThat(dto.name()).isNull(),
                () -> assertThat(dto.category()).isNull(),
                () -> assertThat(dto.role()).isNull(),
                () -> assertThat(dto.isAlcohol()).isNull(),
                () -> assertThat(dto.tier()).isNull(),
                () -> assertThat(dto.frequency()).isNull(),
                () -> assertThat(dto.flavorTags()).isEmpty()
        );
    }
}

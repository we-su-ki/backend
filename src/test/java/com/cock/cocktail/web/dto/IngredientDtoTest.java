package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.Ingredient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class IngredientDtoTest {

    @Test
    @DisplayName("Ingredient → IngredientDto 변환")
    void shouldConvertFromIngredient() {
        var ingredient = new Ingredient("럼");
        ReflectionTestUtils.setField(ingredient, "id", 1L);

        var dto = IngredientDto.from(ingredient);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("럼");
    }

    @Test
    @DisplayName("빈 이름 Ingredient 변환")
    void shouldConvertIngredientWithEmptyName() {
        var ingredient = new Ingredient("");
        ReflectionTestUtils.setField(ingredient, "id", 2L);

        var dto = IngredientDto.from(ingredient);

        assertThat(dto.id()).isEqualTo(2L);
        assertThat(dto.name()).isEmpty();
    }
}

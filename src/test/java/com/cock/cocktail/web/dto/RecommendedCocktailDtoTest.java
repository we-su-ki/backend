package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.MatchedCocktail;
import com.cock.cocktail.domain.SensoryAxis;
import com.cock.cocktail.ingredient.CocktailIngredient;
import com.cock.cocktail.ingredient.Ingredient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RecommendedCocktailDtoTest {

    @Test
    @DisplayName("MatchedCocktail → RecommendedCocktailDto 변환")
    void shouldConvertFromMatchedCocktail() {
        var cocktail = Cocktail.builder()
                .id(1L)
                .name("Mojito")
                .cocktailIngredients(List.of(
                        new CocktailIngredient(null, new Ingredient("럼"), 50),
                        new CocktailIngredient(null, new Ingredient("라임"), 0)
                ))
                .recipe("재료를 섞어서...")
                .sensoryDescriptors(List.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.AROMA, "fruity")
                ))
                .build();

        var matchedCocktail = new MatchedCocktail(
                cocktail,
                0.85,
                "달달한, 과일향 특징을 가진 칵테일입니다.",
                Set.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.AROMA, "fruity")
                )
        );

        var dto = RecommendedCocktailDto.from(matchedCocktail);

        assertAll(
                () -> assertThat(dto.id()).isEqualTo(1L),
                () -> assertThat(dto.name()).isEqualTo("Mojito"),
                () -> assertThat(dto.ingredients()).hasSize(2),
                () -> assertThat(dto.ingredients().get(0).name()).isEqualTo("럼"),
                () -> assertThat(dto.ingredients().get(0).amount()).isEqualTo(50),
                () -> assertThat(dto.recipe()).isEqualTo("재료를 섞어서..."),
                () -> assertThat(dto.score()).isEqualTo(0.85),
                () -> assertThat(dto.reason()).isEqualTo("달달한, 과일향 특징을 가진 칵테일입니다."),
                () -> assertThat(dto.matchedKeywords()).containsExactlyInAnyOrder("sweet", "fruity")
        );
    }

    @Test
    @DisplayName("재료 없는 칵테일 변환")
    void shouldConvertCocktailWithoutIngredients() {
        var cocktail = Cocktail.builder()
                .id(1L)
                .name("Test Cocktail")
                .cocktailIngredients(List.of())
                .recipe("Recipe")
                .sensoryDescriptors(List.of())
                .build();

        var matchedCocktail = new MatchedCocktail(
                cocktail,
                0.5,
                "Test reason",
                Set.of()
        );

        var dto = RecommendedCocktailDto.from(matchedCocktail);

        assertAll(
                () -> assertThat(dto.ingredients()).isEmpty(),
                () -> assertThat(dto.matchedKeywords()).isEmpty()
        );
    }

    @Test
    @DisplayName("matchedDescriptors → matchedKeywords 변환")
    void shouldConvertMatchedDescriptorsToKeywords() {
        var cocktail = Cocktail.builder()
                .id(1L)
                .name("Test")
                .recipe("Recipe")
                .sensoryDescriptors(List.of())
                .build();

        var matchedCocktail = new MatchedCocktail(
                cocktail,
                0.7,
                "Test",
                Set.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.TASTE, "sour"),
                        new DescriptorCode(SensoryAxis.AROMA, "fruity")
                )
        );

        var dto = RecommendedCocktailDto.from(matchedCocktail);

        assertThat(dto.matchedKeywords()).containsExactlyInAnyOrder("sweet", "sour", "fruity");
    }
}

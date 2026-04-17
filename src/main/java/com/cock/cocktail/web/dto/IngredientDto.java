package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.Ingredient;

public record IngredientDto(String name) {
    public static IngredientDto from(Ingredient ingredient) {
        return new IngredientDto(ingredient.getName());
    }
}

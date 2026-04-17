package com.cock.cocktail.web.dto;

import com.cock.cocktail.ingredient.CocktailIngredient;

public record CocktailIngredientDto(String name, int amount) {
    public static CocktailIngredientDto from(CocktailIngredient ci) {
        return new CocktailIngredientDto(ci.getIngredient().getName(), ci.getAmount());
    }
}

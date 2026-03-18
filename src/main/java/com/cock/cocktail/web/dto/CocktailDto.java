package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.Ingredient;

import java.util.List;

public record CocktailDto(
        String id,
        String name,
        List<Ingredient> ingredients,
        List<String> recipe,
        String reason,
        List<String> tags
) {
}

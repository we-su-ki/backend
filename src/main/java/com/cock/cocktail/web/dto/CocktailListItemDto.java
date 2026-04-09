package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.Cocktail;

import java.util.List;

public record CocktailListItemDto(
        Long id,
        String name,
        String imageUrl,
        List<IngredientDto> ingredients,
        TasteProfileDto tasteProfile,
        String recipe,
        double score
) {
    public static CocktailListItemDto from(Cocktail cocktail) {
        return from(cocktail, 0.0);
    }

    public static CocktailListItemDto from(Cocktail cocktail, double score) {
        return new CocktailListItemDto(
                cocktail.getId(),
                cocktail.getName(),
                cocktail.getImageUrl(),
                cocktail.getIngredients().stream().map(IngredientDto::from).toList(),
                TasteProfileDto.from(cocktail.getTasteProfile()),
                cocktail.getRecipe(),
                score
        );
    }
}

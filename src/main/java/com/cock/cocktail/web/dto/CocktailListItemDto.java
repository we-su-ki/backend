package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.CocktailIngredient;
import com.cock.cocktail.domain.taste.TasteMatch;

import java.util.List;

public record CocktailListItemDto(
        String name,
        String imageUrl,
        String glassRaw,
        String garnishRaw,
        String methodRaw,
        String methodCategory,
        List<CocktailIngredient> ingredients,
        Long scoreStrength,
        Long scoreSweetSour,
        String reviewText,
        String sourceUrl,
        TasteProfileDto tasteProfile,
        double matchScore
) {
    public static CocktailListItemDto from(Cocktail cocktail) {
        return from(cocktail, 0.0);
    }

    public static CocktailListItemDto from(Cocktail cocktail, double matchScore) {
        return new CocktailListItemDto(
                cocktail.getName(),
                cocktail.getImageUrl(),
                cocktail.getGlassRaw(),
                cocktail.getGarnishRaw(),
                cocktail.getMethodRaw(),
                cocktail.getMethodCategory(),
                cocktail.getIngredients(),
                cocktail.getScoreStrength(),
                cocktail.getScoreSweetSour(),
                cocktail.getReviewText(),
                cocktail.getSourceUrl(),
                TasteProfileDto.from(cocktail.getTasteProfile()),
                matchScore
        );
    }

    public static CocktailListItemDto from(TasteMatch tm) {
        return from(tm.cocktail(), tm.score());
    }
}

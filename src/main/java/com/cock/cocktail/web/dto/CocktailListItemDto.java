package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.CocktailIngredient;
import com.cock.cocktail.domain.taste.TasteMatch;

import java.util.List;

public record CocktailListItemDto(
        Long id,
        String name,
        String glassRaw,
        String garnishRaw,
        String methodRaw,
        String methodCategory,
        String imageUrl,
        Boolean isAlcohol,
        Double pureAlcoholGrams,
        Double proofInsideBracket,
        List<CocktailIngredient> ingredients,
        Double scoreStrength,
        Double scoreSweetSour,
        String reviewText,
        TasteProfileDto tasteProfile,
        double matchScore
) {
    public static CocktailListItemDto from(Cocktail cocktail) {
        return from(cocktail, 0.0);
    }

    public static CocktailListItemDto from(Cocktail cocktail, double matchScore) {
        return new CocktailListItemDto(
                cocktail.getId(),
                cocktail.getName(),
                cocktail.getGlassRaw(),
                cocktail.getGarnishRaw(),
                cocktail.getMethodRaw(),
                cocktail.getMethodCategory(),
                cocktail.getImageUrl(),
                cocktail.getIsAlcohol(),
                cocktail.getPureAlcoholGrams(),
                cocktail.getProofInsideBracket(),
                cocktail.getIngredients(),
                cocktail.getScoreStrength(),
                cocktail.getScoreSweetSour(),
                cocktail.getReviewText(),
                TasteProfileDto.from(cocktail.getTasteProfile()),
                matchScore
        );
    }

    public static CocktailListItemDto from(TasteMatch tm) {
        return from(tm.cocktail(), tm.score());
    }
}

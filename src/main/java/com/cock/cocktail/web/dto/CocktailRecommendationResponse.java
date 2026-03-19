package com.cock.cocktail.web.dto;

import java.util.List;

public record CocktailRecommendationResponse(
        List<RecommendedCocktailDto> cocktails,
        int count
) {
    public static CocktailRecommendationResponse from(List<RecommendedCocktailDto> cocktails) {
        return new CocktailRecommendationResponse(cocktails, cocktails.size());
    }
}

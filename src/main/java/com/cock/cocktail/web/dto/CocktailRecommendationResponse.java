package com.cock.cocktail.web.dto;

import java.util.List;

public record CocktailRecommendationResponse(
        List<CocktailDto> recommendations,
        AnalyzedKeywords analyzedKeywords
) {
}

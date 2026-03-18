package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.SensoryDescriptors;

import java.util.List;

public record CocktailRecommendationResponse(
        List<CocktailDto> recommendations,
        SensoryDescriptors sensoryDescriptors
) {
}

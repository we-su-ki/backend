package com.cock.cocktail.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CocktailRecommendationRequest(
        @NotBlank(message = "query must not be blank")
        String query
) {
}

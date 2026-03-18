package com.cock.cocktail.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CocktailRecommendationRequest(
        @NotBlank(message = "query는 필수 항목입니다.")
        String query
) {
}

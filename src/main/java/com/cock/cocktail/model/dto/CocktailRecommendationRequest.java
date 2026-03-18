package com.cock.cocktail.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CocktailRecommendationRequest {

    @NotBlank(message = "query는 필수 항목입니다.")
    private String query;
}

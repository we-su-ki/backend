package com.cock.cocktail.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CocktailRecommendationResponse {

    private List<CocktailDto> recommendations;
    private AnalyzedKeywords analyzedKeywords;
}

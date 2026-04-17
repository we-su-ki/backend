package com.cock.cocktail.service.cocktail;

import com.cock.cocktail.domain.MatchedCocktail;

import java.util.List;

public interface CocktailRecommendationService {
    List<MatchedCocktail> recommend(String query);
}

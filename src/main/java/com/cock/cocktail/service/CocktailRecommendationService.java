package com.cock.cocktail.service;

import com.cock.cocktail.domain.MatchedCocktail;

import java.util.List;

public interface CocktailRecommendationService {
    List<MatchedCocktail> recommend(String query);
}

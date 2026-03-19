package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.MatchedCocktail;

import java.util.List;

public record RecommendedCocktailDto(
        Long id,
        String name,
        List<IngredientDto> ingredients,
        String recipe,
        double score,
        String reason,
        List<String> matchedKeywords
) {
    public static RecommendedCocktailDto from(MatchedCocktail matched) {
        var cocktail = matched.getCocktail();
        var ingredients = cocktail.getIngredients().stream()
                .map(IngredientDto::from)
                .toList();
        var matchedKeywords = matched.getMatchedDescriptors().stream()
                .map(com.cock.cocktail.domain.DescriptorCode::value)
                .toList();

        return new RecommendedCocktailDto(
                cocktail.getId(),
                cocktail.getName(),
                ingredients,
                cocktail.getRecipe(),
                matched.getScore(),
                matched.getReason(),
                matchedKeywords
        );
    }

    public static List<RecommendedCocktailDto> from(List<MatchedCocktail> matches) {
        return matches.stream()
                .map(RecommendedCocktailDto::from)
                .toList();
    }
}

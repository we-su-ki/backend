package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.MatchedCocktail;

import java.util.List;

public record RecommendedCocktailDto(
        Long id,
        String name,
        List<CocktailIngredientDto> ingredients,
        String recipe,
        double score,
        String reason,
        List<String> matchedKeywords
) {
    public static RecommendedCocktailDto from(MatchedCocktail matched) {
        var cocktail = matched.cocktail();
        var ingredients = cocktail.getCocktailIngredients().stream()
                .map(CocktailIngredientDto::from)
                .toList();
        var matchedKeywords = matched.matchedDescriptors().stream()
                .map(com.cock.cocktail.domain.descriptor.DescriptorCode::value)
                .toList();

        return new RecommendedCocktailDto(
                cocktail.getId(),
                cocktail.getName(),
                ingredients,
                cocktail.getRecipe(),
                matched.score(),
                matched.reason(),
                matchedKeywords
        );
    }

    public static List<RecommendedCocktailDto> from(List<MatchedCocktail> matches) {
        return matches.stream()
                .map(RecommendedCocktailDto::from)
                .toList();
    }
}

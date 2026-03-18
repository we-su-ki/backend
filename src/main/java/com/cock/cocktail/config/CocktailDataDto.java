package com.cock.cocktail.config;

import com.cock.cocktail.domain.Ingredient;

import java.util.List;
import java.util.Map;

/**
 * JSON 데이터 로딩을 위한 DTO
 */
record CocktailDataDto(
        String name,
        List<Ingredient> ingredients,
        String recipe,
        Map<String, List<String>> tags
) {
}

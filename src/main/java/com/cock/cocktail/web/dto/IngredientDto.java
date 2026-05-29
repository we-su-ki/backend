package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.Ingredient;

import java.util.List;

public record IngredientDto(
        Long id,
        String name,
        String category,
        String role,
        Boolean isAlcohol,
        Integer tier,
        Long frequency,
        List<String> flavorTags,
        TasteProfileDto tasteProfile
) {
    public static IngredientDto from(Ingredient ingredient) {
        return new IngredientDto(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getCategory(),
                ingredient.getRole(),
                ingredient.getIsAlcohol(),
                ingredient.getTier(),
                ingredient.getFrequency(),
                ingredient.getFlavorTags(),
                TasteProfileDto.from(ingredient.getTasteProfile())
        );
    }
}

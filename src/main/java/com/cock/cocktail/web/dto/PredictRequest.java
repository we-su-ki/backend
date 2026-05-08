package com.cock.cocktail.web.dto;

import com.cock.cocktail.application.IngredientAmount;
import com.cock.cocktail.domain.MethodCategory;

import java.util.List;

public record PredictRequest(List<IngredientAmount> ingredients, MethodCategory methodCategory) {
    public PredictRequest {
        if (methodCategory == null) methodCategory = MethodCategory.NONE;
    }
}

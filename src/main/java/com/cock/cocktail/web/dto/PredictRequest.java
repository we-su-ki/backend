package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.IngredientAmount;

import java.util.List;

public record PredictRequest(List<IngredientAmount> ingredients) {
}

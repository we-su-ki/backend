package com.cock.cocktail.web.dto;

import com.cock.cocktail.application.IngredientAmount;

import java.util.List;

public record PredictRequest(List<IngredientAmount> ingredients) {
}

package com.cock.cocktail.application;

import com.cock.cocktail.domain.MethodCategory;

import java.util.List;

public interface TasteProfilePredictor {

    TasteProfilePrediction predict(List<IngredientAmount> ingredients, MethodCategory methodCategory);
}

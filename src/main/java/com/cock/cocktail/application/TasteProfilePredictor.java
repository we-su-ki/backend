package com.cock.cocktail.application;

import com.cock.cocktail.domain.taste.TasteProfile;

import java.util.List;

public interface TasteProfilePredictor {

    TasteProfile predict(List<IngredientAmount> ingredients);
}

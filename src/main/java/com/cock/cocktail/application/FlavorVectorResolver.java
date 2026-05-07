package com.cock.cocktail.application;

import com.cock.cocktail.application.IngredientAmount;
import com.cock.cocktail.domain.taste.TasteProfile;

import java.util.List;

public interface FlavorVectorResolver {

    TasteProfile resolve(List<IngredientAmount> ingredients);
}

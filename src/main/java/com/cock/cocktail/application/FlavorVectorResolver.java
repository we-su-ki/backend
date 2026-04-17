package com.cock.cocktail.application;

import com.cock.cocktail.domain.IngredientAmount;
import com.cock.cocktail.domain.taste.FlavorVector;

import java.util.List;

public interface FlavorVectorResolver {

    FlavorVector resolve(List<IngredientAmount> ingredients);
}

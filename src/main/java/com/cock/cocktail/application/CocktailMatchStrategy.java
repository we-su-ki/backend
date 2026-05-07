package com.cock.cocktail.application;

import com.cock.cocktail.domain.taste.TasteMatch;
import com.cock.cocktail.domain.taste.TasteProfile;

import java.util.List;

public interface CocktailMatchStrategy {

    List<TasteMatch> match(TasteProfile query);
}

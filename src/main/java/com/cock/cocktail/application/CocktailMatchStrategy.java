package com.cock.cocktail.application;

import com.cock.cocktail.domain.taste.TasteMatch;
import com.cock.cocktail.domain.taste.TasteQuery;

import java.util.List;

public interface CocktailMatchStrategy {

    List<TasteMatch> match(TasteQuery query);
}

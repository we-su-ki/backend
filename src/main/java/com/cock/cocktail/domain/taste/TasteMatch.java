package com.cock.cocktail.domain.taste;

import com.cock.cocktail.domain.Cocktail;

public record TasteMatch(Cocktail cocktail, double score) {
}

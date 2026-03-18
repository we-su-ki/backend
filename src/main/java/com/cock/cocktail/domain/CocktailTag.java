package com.cock.cocktail.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record CocktailTag(
        String category,
        String tag
) {
}

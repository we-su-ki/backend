package com.cock.cocktail.domain.taste;

import lombok.Builder;

@Builder
public record TasteProfile(
        double sweet,
        double body,
        double bitter,
        double abv,
        double smoky,
        double sour
) {
}

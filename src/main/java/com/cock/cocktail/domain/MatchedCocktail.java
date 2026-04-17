package com.cock.cocktail.domain;

import com.cock.cocktail.domain.descriptor.DescriptorCode;
import java.util.Objects;
import java.util.Set;

public record MatchedCocktail(
        Cocktail cocktail,
        double score,
        String reason,
        Set<DescriptorCode> matchedDescriptors
) {
    public MatchedCocktail {
        Objects.requireNonNull(cocktail, "cocktail must not be null");
        Objects.requireNonNull(reason, "reason must not be null");
        Objects.requireNonNull(matchedDescriptors, "matchedDescriptors must not be null");

        if (score < 0.0 || score > 1.0) {
            throw new IllegalArgumentException("score must be between 0.0 and 1.0, but was " + score);
        }

        matchedDescriptors = Set.copyOf(matchedDescriptors);
    }
}

package com.cock.cocktail.domain;

import java.util.Objects;
import java.util.Set;

public class MatchedCocktail {

    private final Cocktail cocktail;
    private final double score;
    private final String reason;
    private final Set<DescriptorCode> matchedDescriptors;

    public MatchedCocktail(Cocktail cocktail, double score, String reason, Set<DescriptorCode> matchedDescriptors) {
        Objects.requireNonNull(cocktail, "cocktail must not be null");
        Objects.requireNonNull(reason, "reason must not be null");
        Objects.requireNonNull(matchedDescriptors, "matchedDescriptors must not be null");

        if (score < 0.0 || score > 1.0) {
            throw new IllegalArgumentException("score must be between 0.0 and 1.0, but was " + score);
        }

        this.cocktail = cocktail;
        this.score = score;
        this.reason = reason;
        this.matchedDescriptors = Set.copyOf(matchedDescriptors);
    }

    public Cocktail getCocktail() {
        return cocktail;
    }

    public double getScore() {
        return score;
    }

    public String getReason() {
        return reason;
    }

    public Set<DescriptorCode> getMatchedDescriptors() {
        return matchedDescriptors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchedCocktail that = (MatchedCocktail) o;
        return Double.compare(that.score, score) == 0
                && Objects.equals(cocktail, that.cocktail)
                && Objects.equals(reason, that.reason)
                && Objects.equals(matchedDescriptors, that.matchedDescriptors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cocktail, score, reason, matchedDescriptors);
    }

    @Override
    public String toString() {
        return "MatchedCocktail{" +
                "cocktail=" + cocktail.getName() +
                ", score=" + score +
                ", reason='" + reason + '\'' +
                ", matchedDescriptors=" + matchedDescriptors +
                '}';
    }
}

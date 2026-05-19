package com.cock.cocktail.domain.taste;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;

@Embeddable
@Builder
public record TasteProfile(
        @Column(name = "sweet")       Double sweetness,
        @Column(name = "sour")        Double sourness,
        @Column(name = "bitter")      Double bitterness,
        @Column(name = "umami_salty") Double umamiSalty,
        @Column(name = "fruity")      Double fruity,
        @Column(name = "citrus")      Double citrus,
        @Column(name = "floral")      Double floral,
        @Column(name = "herbal")      Double herbal,
        @Column(name = "spicy")       Double spicy,
        @Column(name = "woody_smoky") Double woodySmoky,
        @Column(name = "body")        Double body,
        @Column(name = "fizzy")       Double fizzy
) {
    public static TasteProfile empty() {
        return new TasteProfile(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    public boolean isEmpty() {
        return sweetness == null
                && sourness == null
                && bitterness == null
                && umamiSalty == null
                && fruity == null
                && citrus == null
                && floral == null
                && herbal == null
                && spicy == null
                && woodySmoky == null
                && body == null
                && fizzy == null;
    }
}

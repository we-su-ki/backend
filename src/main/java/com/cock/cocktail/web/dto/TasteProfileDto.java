package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.taste.TasteProfile;

public record TasteProfileDto(
        double abv,
        double sweetness,
        double sourness,
        double bitterness,
        double umamiSalty,
        double fruity,
        double citrus,
        double floral,
        double herbal,
        double spicy,
        double woodySmoky,
        double body,
        double fizzy
) {
    public static TasteProfileDto from(TasteProfile profile) {
        return new TasteProfileDto(
                v(profile.abv()),
                v(profile.sweetness()),
                v(profile.sourness()),
                v(profile.bitterness()),
                v(profile.umamiSalty()),
                v(profile.fruity()),
                v(profile.citrus()),
                v(profile.floral()),
                v(profile.herbal()),
                v(profile.spicy()),
                v(profile.woodySmoky()),
                v(profile.body()),
                v(profile.fizzy())
        );
    }

    private static double v(Double value) {
        return value != null ? value : 0.0;
    }
}

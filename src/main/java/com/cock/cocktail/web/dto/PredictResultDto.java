package com.cock.cocktail.web.dto;

import com.cock.cocktail.application.TasteProfilePrediction;
import com.cock.cocktail.domain.taste.TasteProfile;

public record PredictResultDto(
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
    public static PredictResultDto from(TasteProfilePrediction prediction) {
        var p = prediction.tasteProfile();
        return new PredictResultDto(
                prediction.abv(),
                v(p.sweetness()),
                v(p.sourness()),
                v(p.bitterness()),
                v(p.umamiSalty()),
                v(p.fruity()),
                v(p.citrus()),
                v(p.floral()),
                v(p.herbal()),
                v(p.spicy()),
                v(p.woodySmoky()),
                v(p.body()),
                v(p.fizzy())
        );
    }

    private static double v(Double value) {
        return value != null ? value : 0.0;
    }
}

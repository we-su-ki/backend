package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.TasteProfile;

public record TasteProfileDto(
        double sweet,
        double body,
        double bitter,
        double abv,
        double smoky,
        double sour
) {
    public static TasteProfileDto from(TasteProfile profile) {
        return new TasteProfileDto(
                profile.sweet(),
                profile.body(),
                profile.bitter(),
                profile.abv(),
                profile.smoky(),
                profile.sour()
        );
    }
}

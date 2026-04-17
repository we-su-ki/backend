package com.cock.cocktail.domain.taste;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FlavorVector {

    private double abv;
    private double carbonated;
    private double sweetness;
    private double sourness;
    private double bitterness;
    private double body;
    private double apple;
    private double banana;
    private double cherry;
    private double citrus;
    private double fruity;
    private double lemon;
    private double orange;
    private double pear;
    private double raisins;
    private double zest;
    private double earthy;
    private double barley;
    private double buttery;
    private double butterscotch;
    private double candy;
    private double chocolate;
    private double cinnamon;
    private double cocoa;
    private double corn;
    private double honey;
    private double tea;
    private double toffee;
    private double clove;
    private double coffee;
    private double floral;
    private double licorice;
    private double malty;
    private double mint;
    private double nutmeg;
    private double peaty;
    private double peppery;
    private double roses;
    private double spices;
    private double sugar;
    private double tobacco;
    private double vanilla;
    private double wood;
    private double sherry;
    private double bitter;
    private double brine;
    private double creamy;
    private double ginger;
    private double herbal;
    private double maple;
    private double nutty;
    private double oak;
    private double salty;
    private double smokey;
    private double sour;
    private double spicy;
    private double sweet;
    private double caramel;

    public TasteProfile computeTasteProfile() {
        double sweet = 0, body = 0, bitter = 0, abv = 0, smoky = 0, sour = 0;

        for (var descriptor : FlavorDescriptor.values()) {
            double value = descriptor.valueFrom(this);
            var tasteProfile = descriptor.tasteProfile();

            sweet += value * tasteProfile.sweet();
            body += value * tasteProfile.body();
            bitter += value * tasteProfile.bitter();
            abv += value * tasteProfile.abv();
            smoky += value * tasteProfile.smoky();
            sour += value * tasteProfile.sour();
        }

        return TasteProfile.builder()
                    .sweet(clamp(sweet))
                    .body(clamp(body))
                    .bitter(clamp(bitter))
                    .abv(clamp(abv))
                    .smoky(clamp(smoky))
                    .sour(clamp(sour))
                .build();
    }

    private double clamp(double value) {
        return Math.clamp(value, 0.0, 5.0);
    }
}

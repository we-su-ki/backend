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

    private static final double[][] WEIGHTS = {
            {0.0, 0.1, 0.0, 1.0, 0.0, 0.0}, // abv
            {0.0, 0.3, 0.0, 0.0, 0.0, 0.1}, // carbonated
            {0.9, 0.3, 0.0, 0.0, 0.0, 0.0}, // sweetness
            {0.0, 0.1, 0.1, 0.0, 0.0, 1.0}, // sourness
            {0.0, 0.2, 1.0, 0.0, 0.0, 0.0}, // bitterness
            {0.2, 1.0, 0.0, 0.0, 0.0, 0.0}, // body
            {0.6, 0.2, 0.0, 0.0, 0.0, 0.2}, // apple
            {0.7, 0.4, 0.0, 0.0, 0.0, 0.1}, // banana
            {0.7, 0.2, 0.1, 0.0, 0.0, 0.2}, // cherry
            {0.3, 0.1, 0.1, 0.0, 0.0, 0.7}, // citrus
            {0.6, 0.2, 0.0, 0.0, 0.0, 0.2}, // fruity
            {0.2, 0.1, 0.1, 0.0, 0.0, 1.0}, // lemon
            {0.5, 0.2, 0.1, 0.0, 0.0, 0.5}, // orange
            {0.6, 0.2, 0.0, 0.0, 0.0, 0.2}, // pear
            {0.7, 0.4, 0.1, 0.0, 0.0, 0.1}, // raisins
            {0.2, 0.1, 0.3, 0.0, 0.0, 0.6}, // zest
            {0.1, 0.3, 0.3, 0.0, 0.2, 0.0}, // earthy
            {0.2, 0.6, 0.2, 0.0, 0.0, 0.0}, // barley
            {0.5, 0.8, 0.0, 0.0, 0.0, 0.0}, // buttery
            {0.9, 0.5, 0.0, 0.0, 0.0, 0.0}, // butterscotch
            {0.9, 0.2, 0.0, 0.0, 0.0, 0.0}, // candy
            {0.5, 0.7, 0.3, 0.0, 0.1, 0.0}, // chocolate
            {0.3, 0.3, 0.2, 0.2, 0.0, 0.0}, // cinnamon
            {0.2, 0.6, 0.7, 0.0, 0.1, 0.0}, // cocoa
            {0.3, 0.5, 0.1, 0.0, 0.0, 0.0}, // corn
            {0.9, 0.3, 0.0, 0.0, 0.0, 0.0}, // honey
            {0.1, 0.3, 0.6, 0.0, 0.0, 0.0}, // tea
            {0.8, 0.5, 0.1, 0.0, 0.0, 0.0}, // toffee
            {0.2, 0.3, 0.3, 0.3, 0.2, 0.0}, // clove
            {0.1, 0.6, 0.9, 0.0, 0.2, 0.0}, // coffee
            {0.3, 0.1, 0.0, 0.0, 0.0, 0.0}, // floral
            {0.3, 0.3, 0.4, 0.0, 0.1, 0.0}, // licorice
            {0.3, 0.7, 0.2, 0.0, 0.0, 0.0}, // malty
            {0.2, 0.1, 0.2, 0.0, 0.0, 0.1}, // mint
            {0.2, 0.3, 0.3, 0.2, 0.1, 0.0}, // nutmeg
            {0.0, 0.4, 0.2, 0.0, 1.0, 0.0}, // peaty
            {0.0, 0.2, 0.2, 0.6, 0.0, 0.0}, // peppery
            {0.3, 0.1, 0.0, 0.0, 0.0, 0.0}, // roses
            {0.2, 0.3, 0.3, 0.3, 0.1, 0.0}, // spices
            {1.0, 0.1, 0.0, 0.0, 0.0, 0.0}, // sugar
            {0.0, 0.5, 0.3, 0.0, 0.8, 0.0}, // tobacco
            {0.8, 0.4, 0.0, 0.0, 0.0, 0.0}, // vanilla
            {0.1, 0.5, 0.2, 0.0, 0.6, 0.0}, // wood
            {0.4, 0.5, 0.1, 0.0, 0.5, 0.1}, // sherry
            {0.0, 0.2, 1.0, 0.0, 0.0, 0.0}, // bitter
            {0.0, 0.2, 0.2, 0.0, 0.0, 0.2}, // brine
            {0.4, 0.9, 0.0, 0.0, 0.0, 0.0}, // creamy
            {0.2, 0.3, 0.2, 0.4, 0.0, 0.1}, // ginger
            {0.1, 0.2, 0.4, 0.0, 0.0, 0.1}, // herbal
            {0.9, 0.4, 0.0, 0.0, 0.0, 0.0}, // maple
            {0.3, 0.7, 0.2, 0.0, 0.0, 0.0}, // nutty
            {0.1, 0.6, 0.3, 0.0, 0.6, 0.0}, // oak
            {0.0, 0.1, 0.1, 0.0, 0.0, 0.0}, // salty
            {0.0, 0.3, 0.2, 0.0, 1.0, 0.0}, // smokey
            {0.0, 0.1, 0.1, 0.0, 0.0, 1.0}, // sour
            {0.0, 0.2, 0.2, 0.5, 0.0, 0.0}, // spicy
            {0.9, 0.3, 0.0, 0.0, 0.0, 0.0}, // sweet
            {0.8, 0.4, 0.1, 0.0, 0.0, 0.0}, // caramel
    };

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
        double[] values = {
                abv, carbonated, sweetness, sourness, bitterness, body,
                apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest,
                earthy, barley,
                buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee,
                clove, coffee,
                floral, licorice, malty, mint, nutmeg,
                peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry,
                bitter, brine, creamy, ginger, herbal, maple, nutty, oak,
                salty, smokey, sour, spicy, sweet, caramel
        };
        double[] axes = new double[6];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < 6; j++) {
                axes[j] += values[i] * WEIGHTS[i][j];
            }
        }
        return new TasteProfile(
                clamp(axes[0]),
                clamp(axes[1]),
                clamp(axes[2]),
                clamp(axes[3]),
                clamp(axes[4]),
                clamp(axes[5])
        );
    }

    private double clamp(double value) {
        return Math.clamp(value, 0.0, 5.0);
    }
}

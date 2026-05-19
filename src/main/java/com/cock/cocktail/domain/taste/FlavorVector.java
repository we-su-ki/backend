package com.cock.cocktail.domain.taste;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FlavorVector {

    private Double abv;
    private Double carbonated;
    private Double sweetness;
    private Double sourness;
    private Double bitterness;
    private Double body;
    private Double apple;
    private Double banana;
    private Double cherry;
    private Double citrus;
    private Double fruity;
    private Double lemon;
    private Double orange;
    private Double pear;
    private Double raisins;
    private Double zest;
    private Double earthy;
    private Double barley;
    private Double buttery;
    private Double butterscotch;
    private Double candy;
    private Double chocolate;
    private Double cinnamon;
    private Double cocoa;
    private Double corn;
    private Double honey;
    private Double tea;
    private Double toffee;
    private Double clove;
    private Double coffee;
    private Double floral;
    private Double licorice;
    private Double malty;
    private Double mint;
    private Double nutmeg;
    private Double peaty;
    private Double peppery;
    private Double roses;
    private Double spices;
    private Double sugar;
    private Double tobacco;
    private Double vanilla;
    private Double wood;
    private Double sherry;
    private Double bitter;
    private Double brine;
    private Double creamy;
    private Double ginger;
    private Double herbal;
    private Double maple;
    private Double nutty;
    private Double oak;
    private Double salty;
    private Double smokey;
    private Double sour;
    private Double spicy;
    private Double sweet;
    private Double caramel;

    public TasteProfile computeTasteProfile() {
        return TasteProfile.builder()
                .sweetness(max(sweetness, sweet, honey, sugar, candy, butterscotch, toffee, caramel, maple, vanilla))
                .sourness(max(sourness, sour, lemon, zest))
                .bitterness(max(bitterness, bitter, coffee, cocoa, tea))
                .umamiSalty(max(salty, brine))
                .fruity(max(fruity, apple, banana, cherry, orange, pear, raisins))
                .citrus(max(citrus, lemon, orange, zest))
                .floral(max(floral, roses))
                .herbal(max(herbal, mint, tea, licorice))
                .spicy(max(spicy, spices, peppery, cinnamon, ginger, nutmeg, clove))
                .woodySmoky(max(wood, smokey, peaty, earthy, oak, tobacco, sherry))
                .body(max(body, buttery, creamy, malty, barley, corn))
                .fizzy(v(carbonated))
                .build();
    }

    private double max(Double... values) {
        double result = 0.0;
        for (var value : values) {
            if (value != null) result = Math.max(result, value);
        }
        return result;
    }

    private double v(Double value) {
        return value != null ? value : 0.0;
    }
}

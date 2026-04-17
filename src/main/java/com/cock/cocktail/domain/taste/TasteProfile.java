package com.cock.cocktail.domain.taste;

public record TasteProfile(
        double sweet,
        double body,
        double bitter,
        double abv,
        double smoky,
        double sour
) {
    public double similarity(TasteQuery query) {
        if (query.isEmpty()) return 0.0;

        double sumSquares = 0.0;
        int count = 0;

        if (query.sweet() != null)  { sumSquares += sq(sweet  - query.sweet());  count++; }
        if (query.body() != null)   { sumSquares += sq(body   - query.body());   count++; }
        if (query.bitter() != null) { sumSquares += sq(bitter - query.bitter()); count++; }
        if (query.abv() != null)    { sumSquares += sq(abv    - query.abv());    count++; }
        if (query.smoky() != null)  { sumSquares += sq(smoky  - query.smoky());  count++; }
        if (query.sour() != null)   { sumSquares += sq(sour   - query.sour());   count++; }

        double maxDist = Math.sqrt(count * 25.0); // 축당 최대 거리 5
        return 1.0 - Math.sqrt(sumSquares) / maxDist;
    }

    private double sq(double v) {
        return v * v;
    }
}

package com.cock.cocktail.infrastructure;

import com.cock.cocktail.domain.taste.TasteMatch;
import com.cock.cocktail.domain.taste.TasteProfile;
import com.cock.cocktail.repository.CocktailRepository;
import com.cock.cocktail.application.CocktailMatchStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CocktailMatchStrategyImpl implements CocktailMatchStrategy {

    private final CocktailRepository cocktailRepository;

    @Override
    public List<TasteMatch> match(TasteProfile query) {
        return cocktailRepository.findAll().stream()
                .map(c -> new TasteMatch(c, similarity(c.getTasteProfile(), query)))
                .filter(m -> m.score() > 0)
                .sorted(Comparator.comparingDouble(TasteMatch::score).reversed())
                .limit(5)
                .toList();
    }

    /**
     * 유사도 = 1 - (실제 거리 / 최대 가능 거리)
     *
     * query에서 null인 축은 비교에서 제외한다.
     * 각 축의 범위는 0~10이므로 축당 최대 제곱 거리 = 100.
     */
    private double similarity(TasteProfile profile, TasteProfile query) {
        if (query.isEmpty()) return 0.0;

        double sumSquares = 0.0;
        int count = 0;

        if (query.sweetness() != null)  { sumSquares += sq(v(profile.sweetness())  - query.sweetness());  count++; }
        if (query.sourness() != null)   { sumSquares += sq(v(profile.sourness())   - query.sourness());   count++; }
        if (query.bitterness() != null) { sumSquares += sq(v(profile.bitterness()) - query.bitterness()); count++; }
        if (query.umamiSalty() != null) { sumSquares += sq(v(profile.umamiSalty()) - query.umamiSalty()); count++; }
        if (query.fruity() != null)     { sumSquares += sq(v(profile.fruity())     - query.fruity());     count++; }
        if (query.citrus() != null)     { sumSquares += sq(v(profile.citrus())     - query.citrus());     count++; }
        if (query.floral() != null)     { sumSquares += sq(v(profile.floral())     - query.floral());     count++; }
        if (query.herbal() != null)     { sumSquares += sq(v(profile.herbal())     - query.herbal());     count++; }
        if (query.spicy() != null)      { sumSquares += sq(v(profile.spicy())      - query.spicy());      count++; }
        if (query.woodySmoky() != null) { sumSquares += sq(v(profile.woodySmoky()) - query.woodySmoky()); count++; }
        if (query.body() != null)       { sumSquares += sq(v(profile.body())       - query.body());       count++; }
        if (query.fizzy() != null)      { sumSquares += sq(v(profile.fizzy())      - query.fizzy());      count++; }

        double maxDist = Math.sqrt(count * 100.0);
        return 1.0 - Math.sqrt(sumSquares) / maxDist;
    }

    private double v(Double value) {
        return value != null ? value : 0.0;
    }

    private double sq(double v) {
        return v * v;
    }
}

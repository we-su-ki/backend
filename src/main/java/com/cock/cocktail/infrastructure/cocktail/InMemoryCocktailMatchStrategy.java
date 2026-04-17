package com.cock.cocktail.infrastructure.cocktail;

import com.cock.cocktail.domain.taste.TasteMatch;
import com.cock.cocktail.domain.taste.TasteProfile;
import com.cock.cocktail.domain.taste.TasteQuery;
import com.cock.cocktail.repository.CocktailRepository;
import com.cock.cocktail.service.cocktail.CocktailMatchStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InMemoryCocktailMatchStrategy implements CocktailMatchStrategy {

    private final CocktailRepository cocktailRepository;

    @Override
    public List<TasteMatch> match(TasteQuery query) {
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
     * 사용자가 지정한 축(sweet, body 등)에 대해서만 유클리드 거리를 계산한다.
     * 지정하지 않은 축은 비교에서 제외한다. "단맛만 강한 칵테일"을 원할 때
     * 바디감이나 도수 차이가 점수에 영향을 주면 안 되기 때문이다.
     *
     * 최대 거리(maxDist)는 지정된 축 수(count)와 각 축의 최대 값 범위(0~5)로부터 산출한다.
     *   - 축 하나의 최대 차이 = 5, 따라서 제곱 = 25
     *   - count개 축의 최대 제곱합 = count * 25
     *   - 최대 유클리드 거리 = sqrt(count * 25)
     * 이 값으로 나누면 실제 거리를 0~1로 정규화할 수 있다.
     * 1에서 빼면 거리가 멀수록 유사도가 낮아지는 [0, 1] 범위의 점수가 된다.
     *   - 1.0 : 쿼리와 완전히 일치
     *   - 0.0 : 쿼리와 가능한 가장 먼 거리
     */
    private double similarity(TasteProfile profile, TasteQuery query) {
        if (query.isEmpty()) return 0.0;

        double sumSquares = 0.0;
        int count = 0;

        if (query.sweet() != null)  { sumSquares += sq(profile.sweet()  - query.sweet());  count++; }
        if (query.body() != null)   { sumSquares += sq(profile.body()   - query.body());   count++; }
        if (query.bitter() != null) { sumSquares += sq(profile.bitter() - query.bitter()); count++; }
        if (query.abv() != null)    { sumSquares += sq(profile.abv()    - query.abv());    count++; }
        if (query.smoky() != null)  { sumSquares += sq(profile.smoky()  - query.smoky());  count++; }
        if (query.sour() != null)   { sumSquares += sq(profile.sour()   - query.sour());   count++; }

        double maxDist = Math.sqrt(count * 25.0); // 축당 최대 거리 5
        return 1.0 - Math.sqrt(sumSquares) / maxDist;
    }

    private double sq(double v) {
        return v * v;
    }
}

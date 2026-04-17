package com.cock.cocktail.infrastructure.matching;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.descriptor.DescriptorCode;
import com.cock.cocktail.domain.MatchedCocktail;
import com.cock.cocktail.domain.descriptor.SensoryDescriptors;
import com.cock.cocktail.repository.CocktailRepository;
import com.cock.cocktail.service.cocktail.CocktailMatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 간단한 칵테일 매칭 구현
 * <p>
 * Repository에서 후보 칵테일을 조회하고,
 * 매칭 점수를 계산하여 상위 3개를 추천합니다.
 */
@Service
@RequiredArgsConstructor
public class SimpleCocktailMatcher implements CocktailMatcher {

    private static final int MAX_RECOMMENDATIONS = 3;

    private final CocktailRepository cocktailRepository;
    private final MatchScoreCalculator scoreCalculator;
    private final ReasonGenerator reasonGenerator;

    @Override
    public List<MatchedCocktail> match(SensoryDescriptors descriptors) {
        if (descriptors.isEmpty()) {
            return List.of();
        }

        // 1. Repository에서 descriptor 기반 후보 칵테일 조회
        var allDescriptors = extractAllDescriptors(descriptors);
        var candidates = cocktailRepository.findByDescriptors(allDescriptors);

        // 2. 각 칵테일에 대해 점수 계산 및 반환
        return candidates.stream()
                .map(cocktail -> {
                    var score = scoreCalculator.calculate(cocktail, descriptors);
                    var matchedDescriptors = findMatchedDescriptors(cocktail, allDescriptors);
                    var reason = reasonGenerator.generate(matchedDescriptors);
                    return new MatchedCocktail(cocktail, score, reason, matchedDescriptors);
                })
                // 3. 점수 > 0 인 칵테일만 선택
                .filter(match -> match.score() > 0.0)
                // 4. 점수 내림차순 정렬
                .sorted(Comparator.comparingDouble(MatchedCocktail::score).reversed())
                // 5. 상위 3개 선정
                .limit(MAX_RECOMMENDATIONS)
                .toList();
    }

    private Set<DescriptorCode> extractAllDescriptors(SensoryDescriptors descriptors) {
        return Stream.of(
                descriptors.taste(),
                descriptors.aroma(),
                descriptors.mouthfeel(),
                descriptors.sensation(),
                descriptors.impression()
        ).flatMap(Set::stream).collect(Collectors.toSet());
    }

    private Set<DescriptorCode> findMatchedDescriptors(Cocktail cocktail, Set<DescriptorCode> queryDescriptors) {
        var cocktailDescriptors = extractAllDescriptors(cocktail.getSensoryDescriptors());
        return queryDescriptors.stream()
                .filter(cocktailDescriptors::contains)
                .collect(Collectors.toSet());
    }
}

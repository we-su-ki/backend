package com.cock.cocktail.infrastructure.matching;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.MatchedCocktail;
import com.cock.cocktail.domain.SensoryDescriptors;
import com.cock.cocktail.repository.CocktailRepository;
import com.cock.cocktail.service.CocktailMatcher;
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
public class SimpleCocktailMatcher implements CocktailMatcher {

    private static final int MAX_RECOMMENDATIONS = 3;

    private final CocktailRepository cocktailRepository;
    private final MatchScoreCalculator scoreCalculator;
    private final ReasonGenerator reasonGenerator;

    public SimpleCocktailMatcher(
            CocktailRepository cocktailRepository,
            MatchScoreCalculator scoreCalculator,
            ReasonGenerator reasonGenerator
    ) {
        this.cocktailRepository = cocktailRepository;
        this.scoreCalculator = scoreCalculator;
        this.reasonGenerator = reasonGenerator;
    }

    @Override
    public List<MatchedCocktail> match(SensoryDescriptors descriptors) {
        if (descriptors.isEmpty()) {
            return List.of();
        }

        // 1. Repository에서 descriptor 기반 후보 칵테일 조회
        var allDescriptors = extractAllDescriptors(descriptors);
        var candidates = cocktailRepository.findByDescriptors(allDescriptors);

        // 2. 각 칵테일에 대해 점수 계산
        var scoredCocktails = candidates.stream()
                .map(cocktail -> {
                    var score = scoreCalculator.calculate(cocktail, descriptors);
                    var matchedDescriptors = findMatchedDescriptors(cocktail, allDescriptors);
                    var reason = reasonGenerator.generate(matchedDescriptors);
                    return new MatchedCocktail(cocktail, score, reason, matchedDescriptors);
                })
                // 3. 점수 > 0 인 칵테일만 선택
                .filter(match -> match.getScore() > 0.0)
                // 4. 점수 내림차순 정렬
                .sorted(Comparator.comparingDouble(MatchedCocktail::getScore).reversed())
                // 5. 상위 3개 선정
                .limit(MAX_RECOMMENDATIONS)
                .toList();

        // 6. MatchedCocktail 리스트 반환
        return scoredCocktails;
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

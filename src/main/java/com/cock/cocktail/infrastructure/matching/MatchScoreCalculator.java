package com.cock.cocktail.infrastructure.matching;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.SensoryDescriptors;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 칵테일과 쿼리 간 매칭 점수 계산기
 * <p>
 * 재현율(Recall) 기반 점수 계산: 사용자가 원하는 특성을 얼마나 만족하는가
 * score = |쿼리 ∩ 칵테일| / |쿼리|
 */
@Component
public class MatchScoreCalculator {

    /**
     * 칵테일과 쿼리 간 매칭 점수를 계산합니다.
     *
     * @param cocktail 매칭할 칵테일
     * @param query 사용자 쿼리에서 추출된 sensory descriptors
     * @return 0.0 ~ 1.0 사이의 매칭 점수
     */
    public double calculate(Cocktail cocktail, SensoryDescriptors query) {
        var queryDescriptors = extractAllDescriptors(query);

        if (queryDescriptors.isEmpty()) {
            return 0.0;
        }

        var cocktailDescriptors = Set.copyOf(cocktail.getSensoryDescriptors().taste());
        cocktailDescriptors = union(cocktailDescriptors, cocktail.getSensoryDescriptors().aroma());
        cocktailDescriptors = union(cocktailDescriptors, cocktail.getSensoryDescriptors().mouthfeel());
        cocktailDescriptors = union(cocktailDescriptors, cocktail.getSensoryDescriptors().sensation());
        cocktailDescriptors = union(cocktailDescriptors, cocktail.getSensoryDescriptors().impression());

        var intersection = queryDescriptors.stream()
                .filter(cocktailDescriptors::contains)
                .collect(Collectors.toSet());

        return (double) intersection.size() / queryDescriptors.size();
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

    private Set<DescriptorCode> union(Set<DescriptorCode> set1, Set<DescriptorCode> set2) {
        return Stream.concat(set1.stream(), set2.stream())
                .collect(Collectors.toSet());
    }
}

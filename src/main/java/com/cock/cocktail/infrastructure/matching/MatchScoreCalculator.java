package com.cock.cocktail.infrastructure.matching;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.SensoryDescriptors;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 재현율(Recall) 기반 점수 계산: score = |쿼리 ∩ 칵테일| / |쿼리|
 */
@Component
public class MatchScoreCalculator {

    public double calculate(Cocktail cocktail, SensoryDescriptors query) {
        var queryDescriptors = extractAllDescriptors(query);

        if (queryDescriptors.isEmpty()) {
            return 0.0;
        }

        var cocktailDescriptors = extractAllDescriptors(cocktail.getSensoryDescriptors());

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
}

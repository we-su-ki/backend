package com.cock.cocktail.infrastructure.matching;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.SensoryAxis;
import com.cock.cocktail.domain.SensoryDescriptors;
import com.cock.cocktail.repository.CocktailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 칵테일 매칭 성능 테스트
 */
@ExtendWith(MockitoExtension.class)
class PerformanceTest {

    @Mock
    private CocktailRepository cocktailRepository;

    private SimpleCocktailMatcher cocktailMatcher;

    @BeforeEach
    void setUp() {
        var scoreCalculator = new MatchScoreCalculator();
        var reasonGenerator = new ReasonGenerator();
        cocktailMatcher = new SimpleCocktailMatcher(cocktailRepository, scoreCalculator, reasonGenerator);

        // 100개의 mock 칵테일 준비
        var mockCocktails = new ArrayList<Cocktail>();
        for (int i = 1; i <= 100; i++) {
            mockCocktails.add(Cocktail.builder()
                    .id((long) i)
                    .name("Cocktail" + i)
                    .recipe("Recipe" + i)
                    .sensoryDescriptors(List.of(
                            new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                            new DescriptorCode(SensoryAxis.AROMA, "fruity"),
                            new DescriptorCode(SensoryAxis.MOUTHFEEL, "smooth")
                    ))
                    .build());
        }
        when(cocktailRepository.findByDescriptors(any())).thenReturn(mockCocktails);
    }

    @Test
    @DisplayName("매칭 성능 - 응답 시간 100ms 이하")
    void shouldCompleteMatchingWithin100ms() {
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .mouthfeel(Set.of(new DescriptorCode(SensoryAxis.MOUTHFEEL, "smooth")))
                .build();

        long startTime = System.currentTimeMillis();
        var matches = cocktailMatcher.match(descriptors);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("매칭 소요 시간: " + duration + "ms");
        System.out.println("매칭된 칵테일 수: " + matches.size());
        assertThat(duration).isLessThan(100);
    }

    @Test
    @DisplayName("여러 번 반복 매칭 - 평균 성능 측정")
    void shouldMeasureAveragePerformance() {
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .build();

        int iterations = 10;
        long totalDuration = 0;

        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            cocktailMatcher.match(descriptors);
            long endTime = System.nanoTime();
            totalDuration += (endTime - startTime);
        }

        long averageDurationNanos = totalDuration / iterations;
        long averageDurationMillis = averageDurationNanos / 1_000_000;

        System.out.println("평균 매칭 소요 시간: " + averageDurationMillis + "ms");
        assertThat(averageDurationMillis).isLessThan(100);
    }

    @Test
    @DisplayName("첫 번째 호출 성능 측정 (콜드 스타트)")
    void shouldMeasureColdStartPerformance() {
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .build();

        long startTime = System.currentTimeMillis();
        var matches = cocktailMatcher.match(descriptors);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("첫 번째 매칭 소요 시간 (콜드 스타트): " + duration + "ms");
        System.out.println("매칭된 칵테일 수: " + matches.size());
        // 콜드 스타트는 더 느릴 수 있으므로 200ms 허용
        assertThat(duration).isLessThan(200);
    }
}

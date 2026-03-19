package com.cock.cocktail.infrastructure.matching;

import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.SensoryAxis;
import com.cock.cocktail.domain.SensoryDescriptors;
import com.cock.cocktail.service.CocktailMatcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 칵테일 매칭 성능 테스트
 */
@SpringBootTest
class PerformanceTest {

    @Autowired
    private CocktailMatcher cocktailMatcher;

    @Test
    @DisplayName("매칭 성능 - 응답 시간 100ms 이하")
    void shouldCompleteMatchingWithin100ms() {
        // given
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .mouthfeel(Set.of(new DescriptorCode(SensoryAxis.MOUTHFEEL, "smooth")))
                .build();

        // when
        long startTime = System.currentTimeMillis();
        var matches = cocktailMatcher.match(descriptors);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // then
        System.out.println("매칭 소요 시간: " + duration + "ms");
        System.out.println("매칭된 칵테일 수: " + matches.size());
        assertThat(duration).isLessThan(100);
    }

    @Test
    @DisplayName("여러 번 반복 매칭 - 평균 성능 측정")
    void shouldMeasureAveragePerformance() {
        // given
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .build();

        int iterations = 10;
        long totalDuration = 0;

        // when
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            cocktailMatcher.match(descriptors);
            long endTime = System.nanoTime();
            totalDuration += (endTime - startTime);
        }

        long averageDurationNanos = totalDuration / iterations;
        long averageDurationMillis = averageDurationNanos / 1_000_000;

        // then
        System.out.println("평균 매칭 소요 시간: " + averageDurationMillis + "ms");
        assertThat(averageDurationMillis).isLessThan(100);
    }

    @Test
    @DisplayName("첫 번째 호출 성능 측정 (콜드 스타트)")
    void shouldMeasureColdStartPerformance() {
        // given
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .build();

        // when
        long startTime = System.currentTimeMillis();
        var matches = cocktailMatcher.match(descriptors);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // then
        System.out.println("첫 번째 매칭 소요 시간 (콜드 스타트): " + duration + "ms");
        System.out.println("매칭된 칵테일 수: " + matches.size());
        // 콜드 스타트는 더 느릴 수 있으므로 200ms 허용
        assertThat(duration).isLessThan(200);
    }
}

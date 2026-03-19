package com.cock.cocktail.infrastructure.matching;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.SensoryAxis;
import com.cock.cocktail.domain.SensoryDescriptors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class MatchScoreCalculatorTest {

    private final MatchScoreCalculator calculator = new MatchScoreCalculator();

    @Test
    @DisplayName("완전 일치 시 점수 1.0")
    void shouldReturnOneWhenPerfectMatch() {
        // given
        var query = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .build();

        var cocktail = Cocktail.builder()
                .name("Test Cocktail")
                .sensoryDescriptors(List.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.AROMA, "fruity")
                ))
                .build();

        // when
        var score = calculator.calculate(cocktail, query);

        // then
        assertThat(score).isEqualTo(1.0);
    }

    @Test
    @DisplayName("부분 일치 시 점수 계산")
    void shouldCalculatePartialMatchScore() {
        // given - 쿼리: [sweet, fruity, smooth], 칵테일: [sweet, fruity, carbonated, summer]
        var query = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .mouthfeel(Set.of(new DescriptorCode(SensoryAxis.MOUTHFEEL, "smooth")))
                .build();

        var cocktail = Cocktail.builder()
                .name("Test Cocktail")
                .sensoryDescriptors(List.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.AROMA, "fruity"),
                        new DescriptorCode(SensoryAxis.SENSATION, "carbonated"),
                        new DescriptorCode(SensoryAxis.IMPRESSION, "summer")
                ))
                .build();

        // when
        var score = calculator.calculate(cocktail, query);

        // then - 교집합 2개 / 쿼리 3개 = 0.67
        assertThat(score).isCloseTo(0.67, within(0.01));
    }

    @Test
    @DisplayName("일치하는 descriptor 없으면 0.0")
    void shouldReturnZeroWhenNoMatch() {
        // given
        var query = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .build();

        var cocktail = Cocktail.builder()
                .name("Test Cocktail")
                .sensoryDescriptors(List.of(
                        new DescriptorCode(SensoryAxis.TASTE, "bitter"),
                        new DescriptorCode(SensoryAxis.AROMA, "herbal")
                ))
                .build();

        // when
        var score = calculator.calculate(cocktail, query);

        // then
        assertThat(score).isEqualTo(0.0);
    }

    @Test
    @DisplayName("쿼리가 비어있으면 0.0")
    void shouldReturnZeroWhenQueryIsEmpty() {
        // given
        var query = SensoryDescriptors.builder().build();

        var cocktail = Cocktail.builder()
                .name("Test Cocktail")
                .sensoryDescriptors(List.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet")
                ))
                .build();

        // when
        var score = calculator.calculate(cocktail, query);

        // then
        assertThat(score).isEqualTo(0.0);
    }

    @Test
    @DisplayName("교집합 비율 계산 - 다양한 축")
    void shouldCalculateIntersectionRatioAcrossAxes() {
        // given - 5개 축에서 3개 일치
        var query = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .mouthfeel(Set.of(new DescriptorCode(SensoryAxis.MOUTHFEEL, "smooth")))
                .sensation(Set.of(new DescriptorCode(SensoryAxis.SENSATION, "carbonated")))
                .impression(Set.of(new DescriptorCode(SensoryAxis.IMPRESSION, "summer")))
                .build();

        var cocktail = Cocktail.builder()
                .name("Test Cocktail")
                .sensoryDescriptors(List.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.AROMA, "fruity"),
                        new DescriptorCode(SensoryAxis.MOUTHFEEL, "smooth")
                ))
                .build();

        // when
        var score = calculator.calculate(cocktail, query);

        // then - 교집합 3개 / 쿼리 5개 = 0.6
        assertThat(score).isEqualTo(0.6);
    }

    @Test
    @DisplayName("동일 축의 복수 descriptors 처리")
    void shouldHandleMultipleDescriptorsInSameAxis() {
        // given - 쿼리: [sweet, sour], 칵테일: [sweet, bitter]
        var query = SensoryDescriptors.builder()
                .taste(Set.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.TASTE, "sour")
                ))
                .build();

        var cocktail = Cocktail.builder()
                .name("Test Cocktail")
                .sensoryDescriptors(List.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.TASTE, "bitter")
                ))
                .build();

        // when
        var score = calculator.calculate(cocktail, query);

        // then - 교집합 1개 (sweet) / 쿼리 2개 = 0.5
        assertThat(score).isEqualTo(0.5);
    }
}

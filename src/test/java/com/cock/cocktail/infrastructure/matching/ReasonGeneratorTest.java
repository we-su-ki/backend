package com.cock.cocktail.infrastructure.matching;

import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.SensoryAxis;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ReasonGeneratorTest {

    private final ReasonGenerator generator = new ReasonGenerator();

    @Test
    @DisplayName("단일 descriptor 매칭 시 이유 생성")
    void shouldGenerateReasonForSingleDescriptor() {
        // given
        var matchedDescriptors = Set.of(
                new DescriptorCode(SensoryAxis.TASTE, "sweet")
        );

        // when
        var reason = generator.generate(matchedDescriptors);

        // then
        assertThat(reason).contains("달달한");
    }

    @Test
    @DisplayName("복수 descriptor 매칭 시 이유 생성")
    void shouldGenerateReasonForMultipleDescriptors() {
        // given
        var matchedDescriptors = Set.of(
                new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                new DescriptorCode(SensoryAxis.AROMA, "fruity")
        );

        // when
        var reason = generator.generate(matchedDescriptors);

        // then
        assertThat(reason)
                .contains("달달한")
                .contains("과일향");
    }

    @Test
    @DisplayName("축별 descriptor 조합 - 맛과 향")
    void shouldGenerateReasonForTasteAndAroma() {
        // given
        var matchedDescriptors = Set.of(
                new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                new DescriptorCode(SensoryAxis.AROMA, "fruity")
        );

        // when
        var reason = generator.generate(matchedDescriptors);

        // then
        assertThat(reason)
                .contains("달달한")
                .contains("과일향")
                .contains("칵테일");
    }

    @Test
    @DisplayName("축별 descriptor 조합 - 전체 축")
    void shouldGenerateReasonForAllAxes() {
        // given
        var matchedDescriptors = Set.of(
                new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                new DescriptorCode(SensoryAxis.AROMA, "fruity"),
                new DescriptorCode(SensoryAxis.MOUTHFEEL, "smooth"),
                new DescriptorCode(SensoryAxis.SENSATION, "carbonated"),
                new DescriptorCode(SensoryAxis.IMPRESSION, "summer")
        );

        // when
        var reason = generator.generate(matchedDescriptors);

        // then
        assertThat(reason)
                .contains("달달한")
                .contains("과일향")
                .contains("부드러운")
                .contains("톡 쏘는")
                .contains("여름");
    }

    @Test
    @DisplayName("빈 매칭 시 기본 이유")
    void shouldGenerateDefaultReasonForEmptyMatch() {
        // given
        var matchedDescriptors = Set.<DescriptorCode>of();

        // when
        var reason = generator.generate(matchedDescriptors);

        // then
        assertThat(reason).isEqualTo("추천 칵테일입니다.");
    }

    @Test
    @DisplayName("동일 축의 복수 descriptors 처리")
    void shouldHandleMultipleDescriptorsInSameAxis() {
        // given
        var matchedDescriptors = Set.of(
                new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                new DescriptorCode(SensoryAxis.TASTE, "sour")
        );

        // when
        var reason = generator.generate(matchedDescriptors);

        // then
        assertThat(reason)
                .contains("달달한")
                .contains("새콤한");
    }

    @Test
    @DisplayName("알 수 없는 descriptor는 무시")
    void shouldIgnoreUnknownDescriptor() {
        // given
        var matchedDescriptors = Set.of(
                new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                new DescriptorCode(SensoryAxis.TASTE, "unknown")
        );

        // when
        var reason = generator.generate(matchedDescriptors);

        // then
        assertThat(reason).contains("달달한");
    }
}

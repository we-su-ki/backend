package com.cock.cocktail.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SensoryDescriptorsTest {

    @Test
    @DisplayName("정상 생성 - 5개 축 모두 제공")
    void shouldCreateWithAllAxes() {
        // given
        var taste = List.of(
                new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                new DescriptorCode(SensoryAxis.TASTE, "sour")
        );
        var aroma = List.of(
                new DescriptorCode(SensoryAxis.AROMA, "fruity"),
                new DescriptorCode(SensoryAxis.AROMA, "lime")
        );
        var mouthfeel = List.of(new DescriptorCode(SensoryAxis.MOUTHFEEL, "smooth"));
        var sensation = List.of(new DescriptorCode(SensoryAxis.SENSATION, "carbonated"));
        var impression = List.of(
                new DescriptorCode(SensoryAxis.IMPRESSION, "refreshing"),
                new DescriptorCode(SensoryAxis.IMPRESSION, "summer")
        );

        // when
        var descriptors = new SensoryDescriptors(Map.of(
                SensoryAxis.TASTE, Set.copyOf(taste),
                SensoryAxis.AROMA, Set.copyOf(aroma),
                SensoryAxis.MOUTHFEEL, Set.copyOf(mouthfeel),
                SensoryAxis.SENSATION, Set.copyOf(sensation),
                SensoryAxis.IMPRESSION, Set.copyOf(impression)
        ));

        // then
        assertAll(
                () -> assertThat(descriptors.codeValues(SensoryAxis.TASTE)).containsExactlyInAnyOrder("sweet", "sour"),
                () -> assertThat(descriptors.codeValues(SensoryAxis.AROMA)).containsExactlyInAnyOrder("fruity", "lime"),
                () -> assertThat(descriptors.codeValues(SensoryAxis.MOUTHFEEL)).containsExactly("smooth"),
                () -> assertThat(descriptors.codeValues(SensoryAxis.SENSATION)).containsExactly("carbonated"),
                () -> assertThat(descriptors.codeValues(SensoryAxis.IMPRESSION)).containsExactlyInAnyOrder("refreshing", "summer")
        );
    }

    @Test
    @DisplayName("빈 리스트로 생성")
    void shouldCreateWithEmptyLists() {
        // given & when
        var descriptors = new SensoryDescriptors(Map.of());

        // then
        assertAll(
                () -> assertThat(descriptors.taste()).isEmpty(),
                () -> assertThat(descriptors.aroma()).isEmpty(),
                () -> assertThat(descriptors.mouthfeel()).isEmpty(),
                () -> assertThat(descriptors.sensation()).isEmpty(),
                () -> assertThat(descriptors.impression()).isEmpty()
        );
    }

    @Test
    @DisplayName("axis 불일치 descriptor 포함 시 예외")
    void shouldThrowWhenAxisMismatched() {
        assertThatThrownBy(() -> new SensoryDescriptors(Map.of(
                SensoryAxis.TASTE, Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity"))
        ))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("isEmpty - 하나라도 있으면 false")
    void shouldReturnFalseWhenAnyAxisHasDescriptors() {
        // given
        var descriptors = new SensoryDescriptors(Map.of(
                SensoryAxis.TASTE, Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet"))
        ));

        // when & then
        assertThat(descriptors.isEmpty()).isFalse();
    }
}

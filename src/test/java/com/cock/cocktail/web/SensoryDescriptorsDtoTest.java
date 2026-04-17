package com.cock.cocktail.web;

import com.cock.cocktail.domain.descriptor.DescriptorCode;
import com.cock.cocktail.domain.descriptor.SensoryAxis;
import com.cock.cocktail.domain.descriptor.SensoryDescriptors;
import com.cock.cocktail.web.dto.SensoryDescriptorsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SensoryDescriptorsDtoTest {

    @Test
    @DisplayName("SensoryDescriptors를 DTO로 변환")
    void shouldConvertFromSensoryDescriptors() {
        var sensoryDescriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .mouthfeel(Set.of(new DescriptorCode(SensoryAxis.MOUTHFEEL, "smooth")))
                .sensation(Set.of(new DescriptorCode(SensoryAxis.SENSATION, "carbonated")))
                .impression(Set.of(new DescriptorCode(SensoryAxis.IMPRESSION, "summer")))
                .build();

        var dto = SensoryDescriptorsDto.from(sensoryDescriptors);

        assertAll(
                () -> assertThat(dto.taste()).containsExactly("sweet"),
                () -> assertThat(dto.aroma()).containsExactly("fruity"),
                () -> assertThat(dto.mouthfeel()).containsExactly("smooth"),
                () -> assertThat(dto.sensation()).containsExactly("carbonated"),
                () -> assertThat(dto.impression()).containsExactly("summer")
        );
    }

    @Test
    @DisplayName("빈 SensoryDescriptors를 DTO로 변환")
    void shouldConvertEmptySensoryDescriptors() {
        var sensoryDescriptors = SensoryDescriptors.builder().build();

        var dto = SensoryDescriptorsDto.from(sensoryDescriptors);

        assertAll(
                () -> assertThat(dto.taste()).isEmpty(),
                () -> assertThat(dto.aroma()).isEmpty(),
                () -> assertThat(dto.mouthfeel()).isEmpty(),
                () -> assertThat(dto.sensation()).isEmpty(),
                () -> assertThat(dto.impression()).isEmpty()
        );
    }

    @Test
    @DisplayName("일부 축만 있는 SensoryDescriptors를 DTO로 변환")
    void shouldConvertPartialSensoryDescriptors() {
        var sensoryDescriptors = SensoryDescriptors.builder()
                .taste(Set.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.TASTE, "sour")
                ))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .build();

        var dto = SensoryDescriptorsDto.from(sensoryDescriptors);

        assertAll(
                () -> assertThat(dto.taste()).containsExactlyInAnyOrder("sweet", "sour"),
                () -> assertThat(dto.aroma()).containsExactly("fruity"),
                () -> assertThat(dto.mouthfeel()).isEmpty(),
                () -> assertThat(dto.sensation()).isEmpty(),
                () -> assertThat(dto.impression()).isEmpty()
        );
    }
}

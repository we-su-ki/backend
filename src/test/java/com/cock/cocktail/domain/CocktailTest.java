package com.cock.cocktail.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CocktailTest {

    @Test
    @DisplayName("DescriptorCode 목록을 SensoryDescriptors로 투영")
    void shouldProjectDescriptorCodesToSensoryDescriptors() {
        var cocktail = Cocktail.builder()
                .sensoryDescriptors(List.of(
                        new DescriptorCode(SensoryAxis.TASTE, "달달한"),
                        new DescriptorCode(SensoryAxis.AROMA, "과일향"),
                        new DescriptorCode(SensoryAxis.MOUTHFEEL, "부드러운"),
                        new DescriptorCode(SensoryAxis.SENSATION, "톡 쏘는"),
                        new DescriptorCode(SensoryAxis.IMPRESSION, "여름에 어울리는")
                ))
                .build();

        var sensoryDescriptors = cocktail.getSensoryDescriptors();

        assertAll(
                () -> assertThat(sensoryDescriptors.codeValues(SensoryAxis.TASTE)).contains("달달한"),
                () -> assertThat(sensoryDescriptors.codeValues(SensoryAxis.AROMA)).contains("과일향"),
                () -> assertThat(sensoryDescriptors.codeValues(SensoryAxis.MOUTHFEEL)).contains("부드러운"),
                () -> assertThat(sensoryDescriptors.codeValues(SensoryAxis.SENSATION)).contains("톡 쏘는"),
                () -> assertThat(sensoryDescriptors.codeValues(SensoryAxis.IMPRESSION)).contains("여름에 어울리는")
        );
    }
}

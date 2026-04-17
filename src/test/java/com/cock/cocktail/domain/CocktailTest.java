package com.cock.cocktail.domain;

import com.cock.cocktail.domain.descriptor.DescriptorCode;
import com.cock.cocktail.domain.descriptor.SensoryAxis;
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
                () -> assertThat(sensoryDescriptors.taste().stream().map(d -> d.value()).toList()).contains("달달한"),
                () -> assertThat(sensoryDescriptors.aroma().stream().map(d -> d.value()).toList()).contains("과일향"),
                () -> assertThat(sensoryDescriptors.mouthfeel().stream().map(d -> d.value()).toList()).contains("부드러운"),
                () -> assertThat(sensoryDescriptors.sensation().stream().map(d -> d.value()).toList()).contains("톡 쏘는"),
                () -> assertThat(sensoryDescriptors.impression().stream().map(d -> d.value()).toList()).contains("여름에 어울리는")
        );
    }
}

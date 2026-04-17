package com.cock.cocktail.domain.taste;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertAll;

class FlavorVectorTest {

    @Test
    @DisplayName("비어 있는 FlavorVector는 모든 맛 축이 0.0이다")
    void shouldReturnZeroTasteProfileWhenFlavorVectorIsEmpty() {
        var flavorVector = new FlavorVector();

        var profile = flavorVector.computeTasteProfile();

        assertThat(profile).isEqualTo(new TasteProfile(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
    }

    @Test
    @DisplayName("단일 flavor 값은 정의된 가중치대로 TasteProfile에 반영된다")
    void shouldProjectSingleFlavorUsingWeights() {
        var flavorVector = FlavorVector.builder()
                .sweetness(2.0)
                .build();

        var profile = flavorVector.computeTasteProfile();

        assertAll(
                () -> assertThat(profile.sweet()).isEqualTo(1.8),
                () -> assertThat(profile.body()).isEqualTo(0.6),
                () -> assertThat(profile.bitter()).isEqualTo(0.0),
                () -> assertThat(profile.abv()).isEqualTo(0.0),
                () -> assertThat(profile.smoky()).isEqualTo(0.0),
                () -> assertThat(profile.sour()).isEqualTo(0.0)
        );
    }

    @Test
    @DisplayName("여러 flavor 값은 축별로 누적 계산된다")
    void shouldAccumulateMultipleFlavorContributions() {
        var flavorVector = FlavorVector.builder()
                .abv(1.0)
                .citrus(2.0)
                .oak(1.5)
                .build();

        var profile = flavorVector.computeTasteProfile();

        // abv(1.0)     ->    [sweet 0.0, body 0.1, bitter 0.0, abv 1.0, smoky 0.0, sour 0.0]
        // citrus(2.0)  ->    [sweet 0.6, body 0.2, bitter 0.2, abv 0.0, smoky 0.0, sour 1.4]
        // oak(1.5)     ->    [sweet 0.15, body 0.9, bitter 0.45, abv 0.0, smoky 0.9, sour 0.0]
        double expectedSweet = 0.0 + 0.6 + 0.15;
        double expectedBody = 0.1 + 0.2 + 0.9;
        double expectedBitter = 0.0 + 0.2 + 0.45;
        double expectedAbv = 1.0 + 0.0 + 0.0;
        double expectedSmoky = 0.0 + 0.0 + 0.9;
        double expectedSour = 0.0 + 1.4 + 0.0;

        assertAll(
                () -> assertThat(profile.sweet()).isCloseTo(expectedSweet, within(0.000000001)),
                () -> assertThat(profile.body()).isCloseTo(expectedBody, within(0.000000001)),
                () -> assertThat(profile.bitter()).isCloseTo(expectedBitter, within(0.000000001)),
                () -> assertThat(profile.abv()).isCloseTo(expectedAbv, within(0.000000001)),
                () -> assertThat(profile.smoky()).isCloseTo(expectedSmoky, within(0.000000001)),
                () -> assertThat(profile.sour()).isCloseTo(expectedSour, within(0.000000001))
        );
    }

    @Test
    @DisplayName("계산된 값이 5.0을 넘으면 축별로 5.0으로 고정된다")
    void shouldClampEachAxisToFive() {
        var flavorVector = FlavorVector.builder()
                .sugar(10.0)
                .build();

        var profile = flavorVector.computeTasteProfile();

        assertAll(
                () -> assertThat(profile.sweet()).isEqualTo(5.0),
                () -> assertThat(profile.body()).isEqualTo(1.0),
                () -> assertThat(profile.bitter()).isEqualTo(0.0),
                () -> assertThat(profile.abv()).isEqualTo(0.0),
                () -> assertThat(profile.smoky()).isEqualTo(0.0),
                () -> assertThat(profile.sour()).isEqualTo(0.0)
        );
    }
}

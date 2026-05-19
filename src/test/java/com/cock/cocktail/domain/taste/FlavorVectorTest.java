package com.cock.cocktail.domain.taste;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FlavorVectorTest {

    @Test
    @DisplayName("비어 있는 FlavorVector는 모든 맛 축이 0.0이다")
    void shouldReturnZeroTasteProfileWhenFlavorVectorIsEmpty() {
        var profile = new FlavorVector().computeTasteProfile();

        assertThat(profile).isEqualTo(TasteProfile.empty());
    }

    @Test
    @DisplayName("sweetness 값은 TasteProfile.sweetness에 직접 반영된다")
    void shouldMapSweetnessDirectly() {
        var profile = FlavorVector.builder()
                .sweetness(2.0)
                .build()
                .computeTasteProfile();

        assertAll(
                () -> assertThat(profile.sweetness()).isEqualTo(2.0),
                () -> assertThat(profile.sourness()).isEqualTo(0.0),
                () -> assertThat(profile.bitterness()).isEqualTo(0.0),
                () -> assertThat(profile.woodySmoky()).isEqualTo(0.0),
                () -> assertThat(profile.body()).isEqualTo(0.0)
        );
    }

    @Test
    @DisplayName("여러 축이 독립적으로 계산된다")
    void shouldMapMultipleFieldsIndependently() {
        var profile = FlavorVector.builder()
                .citrus(3.0)
                .oak(2.5)
                .build()
                .computeTasteProfile();

        assertAll(
                () -> assertThat(profile.citrus()).isEqualTo(3.0),
                () -> assertThat(profile.woodySmoky()).isEqualTo(2.5)
        );
    }

    @Test
    @DisplayName("같은 그룹 내 필드 중 최댓값이 반영된다")
    void shouldUseMaxValueWithinGroup() {
        // woodySmoky 그룹: wood, smokey, peaty, earthy, oak, tobacco, sherry
        var profile = FlavorVector.builder()
                .wood(3.0)
                .smokey(5.0)
                .oak(4.0)
                .build()
                .computeTasteProfile();

        assertThat(profile.woodySmoky()).isEqualTo(5.0);
    }

    @Test
    @DisplayName("umamiSalty는 salty와 brine 중 최댓값이다")
    void shouldMapUmamiSaltyFromSaltyAndBrine() {
        var profile = FlavorVector.builder()
                .salty(2.0)
                .brine(4.0)
                .build()
                .computeTasteProfile();

        assertThat(profile.umamiSalty()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("fizzy는 carbonated에서 직접 매핑된다")
    void shouldMapFizzyFromCarbonated() {
        var profile = FlavorVector.builder()
                .carbonated(7.0)
                .build()
                .computeTasteProfile();

        assertThat(profile.fizzy()).isEqualTo(7.0);
    }

    @Test
    @DisplayName("lemon은 sourness와 citrus 두 축에 동시에 반영된다")
    void shouldMapLemonToBothSournessAndCitrus() {
        var profile = FlavorVector.builder()
                .lemon(6.0)
                .build()
                .computeTasteProfile();

        assertAll(
                () -> assertThat(profile.sourness()).isEqualTo(6.0),
                () -> assertThat(profile.citrus()).isEqualTo(6.0)
        );
    }

    @Test
    @DisplayName("tea는 bitterness와 herbal 두 축에 동시에 반영된다")
    void shouldMapTeaToBothBitternessAndHerbal() {
        var profile = FlavorVector.builder()
                .tea(5.0)
                .build()
                .computeTasteProfile();

        assertAll(
                () -> assertThat(profile.bitterness()).isEqualTo(5.0),
                () -> assertThat(profile.herbal()).isEqualTo(5.0)
        );
    }
}

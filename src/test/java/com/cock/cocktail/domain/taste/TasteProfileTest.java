package com.cock.cocktail.domain.taste;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TasteProfileTest {

    @Test
    @DisplayName("empty()는 13개 축 모두 0.0이다")
    void shouldReturnAllZerosForEmpty() {
        var profile = TasteProfile.empty();

        assertAll(
                () -> assertThat(profile.abv()).isEqualTo(0.0),
                () -> assertThat(profile.sweetness()).isEqualTo(0.0),
                () -> assertThat(profile.sourness()).isEqualTo(0.0),
                () -> assertThat(profile.bitterness()).isEqualTo(0.0),
                () -> assertThat(profile.umamiSalty()).isEqualTo(0.0),
                () -> assertThat(profile.fruity()).isEqualTo(0.0),
                () -> assertThat(profile.citrus()).isEqualTo(0.0),
                () -> assertThat(profile.floral()).isEqualTo(0.0),
                () -> assertThat(profile.herbal()).isEqualTo(0.0),
                () -> assertThat(profile.spicy()).isEqualTo(0.0),
                () -> assertThat(profile.woodySmoky()).isEqualTo(0.0),
                () -> assertThat(profile.body()).isEqualTo(0.0),
                () -> assertThat(profile.fizzy()).isEqualTo(0.0)
        );
    }

    @Test
    @DisplayName("isEmpty()는 모든 축이 null일 때 true이다")
    void shouldBeTrueWhenAllNull() {
        assertThat(TasteProfile.builder().build().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("isEmpty()는 하나라도 값이 있으면 false이다")
    void shouldBeFalseWhenAnyFieldSet() {
        assertThat(TasteProfile.builder().sweetness(5.0).build().isEmpty()).isFalse();
    }

    @Test
    @DisplayName("isEmpty()는 0.0 값을 null로 취급하지 않는다")
    void shouldBeFalseWhenAllZero() {
        assertThat(TasteProfile.empty().isEmpty()).isFalse();
    }
}

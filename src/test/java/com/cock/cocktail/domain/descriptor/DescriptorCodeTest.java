package com.cock.cocktail.domain.descriptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DescriptorCodeTest {

    @Test
    @DisplayName("생성 시 값은 소문자로 정규화")
    void shouldNormalizeToLowerCase() {
        var descriptorCode = new DescriptorCode(SensoryAxis.TASTE, " Sweet ");

        assertThat(descriptorCode.value()).isEqualTo("sweet");
    }

    @Test
    @DisplayName("축이 null이면 예외")
    void shouldThrowWhenAxisIsNull() {
        assertThatThrownBy(() -> new DescriptorCode(null, "sweet"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("값이 blank면 예외")
    void shouldThrowWhenValueIsBlank() {
        assertThatThrownBy(() -> new DescriptorCode(SensoryAxis.TASTE, "   "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

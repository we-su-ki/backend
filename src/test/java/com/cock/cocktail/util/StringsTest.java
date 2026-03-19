package com.cock.cocktail.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringsTest {

    @Test
    @DisplayName("requireNotBlank: 정상 문자열")
    void requireNotBlank_validString() {
        String result = Strings.requireNotBlank("hello", "error");

        assertThat(result).isEqualTo("hello");
    }

    @Test
    @DisplayName("requireNotBlank: null이면 예외")
    void requireNotBlank_nullThrowsException() {
        assertThatThrownBy(() -> Strings.requireNotBlank(null, "value is null"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("value is null");
    }

    @Test
    @DisplayName("requireNotBlank: 빈 문자열이면 예외")
    void requireNotBlank_emptyStringThrowsException() {
        assertThatThrownBy(() -> Strings.requireNotBlank("", "value is empty"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("value is empty");
    }

    @Test
    @DisplayName("requireNotBlank: 공백만 있으면 예외")
    void requireNotBlank_blankStringThrowsException() {
        assertThatThrownBy(() -> Strings.requireNotBlank("   ", "value is blank"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("value is blank");
    }

    @Test
    @DisplayName("trimAndLowerCase: 앞뒤 공백 제거 및 소문자 변환")
    void trimAndLowerCase_trimAndConvert() {
        String result = Strings.trimAndLowerCase("  HELLO World  ");

        assertThat(result).isEqualTo("hello world");
    }

    @Test
    @DisplayName("trimAndLowerCase: 이미 소문자이고 공백 없으면 그대로")
    void trimAndLowerCase_alreadyLowercase() {
        String result = Strings.trimAndLowerCase("hello");

        assertThat(result).isEqualTo("hello");
    }

    @Test
    @DisplayName("trimAndLowerCase: 빈 문자열")
    void trimAndLowerCase_emptyString() {
        String result = Strings.trimAndLowerCase("");

        assertThat(result).isEmpty();
    }
}

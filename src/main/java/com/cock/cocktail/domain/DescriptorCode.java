package com.cock.cocktail.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Objects;

/**
 * 감각 축별 정규화된 descriptor 코드 값 객체.
 */
@Embeddable
public record DescriptorCode(
        @Enumerated(EnumType.STRING)
        SensoryAxis axis,
        @Column(name = "value")
        String value
) {
    public DescriptorCode {
        Objects.requireNonNull(axis, "axis must not be null");
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }
        value = value.trim().toLowerCase();
    }
}

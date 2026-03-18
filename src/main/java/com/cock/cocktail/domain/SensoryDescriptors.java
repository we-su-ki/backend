package com.cock.cocktail.domain;

import static com.cock.cocktail.domain.SensoryAxis.AROMA;
import static com.cock.cocktail.domain.SensoryAxis.IMPRESSION;
import static com.cock.cocktail.domain.SensoryAxis.MOUTHFEEL;
import static com.cock.cocktail.domain.SensoryAxis.SENSATION;
import static com.cock.cocktail.domain.SensoryAxis.TASTE;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 감각 축별 descriptor 모음
 * <p>
 * 사용자 쿼리에서 추출된 sensory descriptor들을 감각 축별로 관리합니다.
 */
public class SensoryDescriptors {

    private final Map<SensoryAxis, Set<DescriptorCode>> descriptorsByAxis;

    public SensoryDescriptors(List<DescriptorCode> descriptors) {
        this(descriptors.stream().collect(
                Collectors.groupingBy(
                        DescriptorCode::axis,
                        Collectors.mapping(descriptor -> descriptor, Collectors.toSet()))
                )
        );
    }

    public SensoryDescriptors(Map<SensoryAxis, Set<DescriptorCode>> descriptorsByAxis) {
        Objects.requireNonNull(descriptorsByAxis, "descriptorsByAxis must not be null");
        this.descriptorsByAxis = Map.of(
                TASTE, normalizeAxis(descriptorsByAxis.get(TASTE), TASTE),
                AROMA, normalizeAxis(descriptorsByAxis.get(AROMA), AROMA),
                MOUTHFEEL, normalizeAxis(descriptorsByAxis.get(MOUTHFEEL), MOUTHFEEL),
                SENSATION, normalizeAxis(descriptorsByAxis.get(SENSATION), SENSATION),
                IMPRESSION, normalizeAxis(descriptorsByAxis.get(IMPRESSION), IMPRESSION)
        );
    }

    public Set<DescriptorCode> get(SensoryAxis axis) {
        return descriptorsByAxis.getOrDefault(axis, Set.of());
    }

    public Set<DescriptorCode> taste() {
        return get(TASTE);
    }

    public Set<DescriptorCode> aroma() {
        return get(AROMA);
    }

    public Set<DescriptorCode> mouthfeel() {
        return get(MOUTHFEEL);
    }

    public Set<DescriptorCode> sensation() {
        return get(SENSATION);
    }

    public Set<DescriptorCode> impression() {
        return get(IMPRESSION);
    }

    public Set<String> codeValues(SensoryAxis axis) {
        return get(axis).stream()
                .map(DescriptorCode::value)
                .collect(Collectors.toUnmodifiableSet());
    }

    public boolean isEmpty() {
        return descriptorsByAxis.values().stream()
                .allMatch(Set::isEmpty);
    }

    private static Set<DescriptorCode> normalizeAxis(Set<DescriptorCode> descriptors, SensoryAxis axis) {
        if (descriptors == null || descriptors.isEmpty()) {
            return Set.of();
        }

        return descriptors.stream()
                .peek(descriptor -> {
                    if (descriptor.axis() != axis) {
                        throw new IllegalArgumentException("Descriptor axis mismatch: expected " + axis
                                + " but was " + descriptor.axis());
                    }
                })
                .collect(Collectors.toUnmodifiableSet());
    }
}

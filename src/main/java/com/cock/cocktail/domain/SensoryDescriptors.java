package com.cock.cocktail.domain;

import static com.cock.cocktail.domain.SensoryAxis.AROMA;
import static com.cock.cocktail.domain.SensoryAxis.IMPRESSION;
import static com.cock.cocktail.domain.SensoryAxis.MOUTHFEEL;
import static com.cock.cocktail.domain.SensoryAxis.SENSATION;
import static com.cock.cocktail.domain.SensoryAxis.TASTE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SensoryDescriptors {

    private final List<DescriptorCode> descriptors;

    public static Builder builder() {
        return new Builder();
    }

    public SensoryDescriptors(List<DescriptorCode> descriptors) {
        Objects.requireNonNull(descriptors, "descriptors must not be null");
        this.descriptors = List.copyOf(descriptors);
    }

    public Set<DescriptorCode> get(SensoryAxis axis) {
        return descriptors.stream()
                .filter(descriptor -> descriptor.axis() == axis)
                .collect(Collectors.toUnmodifiableSet());
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
        return descriptors.stream()
                .filter(descriptor -> descriptor.axis() == axis)
                .map(DescriptorCode::value)
                .collect(Collectors.toUnmodifiableSet());
    }

    public boolean isEmpty() {
        return descriptors.isEmpty();
    }

    public static class Builder {
        private Set<DescriptorCode> taste = Set.of();
        private Set<DescriptorCode> aroma = Set.of();
        private Set<DescriptorCode> mouthfeel = Set.of();
        private Set<DescriptorCode> sensation = Set.of();
        private Set<DescriptorCode> impression = Set.of();

        public Builder taste(Set<DescriptorCode> taste) {
            this.taste = validateAxis(taste, TASTE);
            return this;
        }

        public Builder aroma(Set<DescriptorCode> aroma) {
            this.aroma = validateAxis(aroma, AROMA);
            return this;
        }

        public Builder mouthfeel(Set<DescriptorCode> mouthfeel) {
            this.mouthfeel = validateAxis(mouthfeel, MOUTHFEEL);
            return this;
        }

        public Builder sensation(Set<DescriptorCode> sensation) {
            this.sensation = validateAxis(sensation, SENSATION);
            return this;
        }

        public Builder impression(Set<DescriptorCode> impression) {
            this.impression = validateAxis(impression, IMPRESSION);
            return this;
        }

        private Set<DescriptorCode> validateAxis(Set<DescriptorCode> descriptors, SensoryAxis expectedAxis) {
            if (descriptors == null) {
                return Set.of();
            }
            descriptors.forEach(descriptor -> {
                if (descriptor.axis() != expectedAxis) {
                    throw new IllegalArgumentException("Descriptor axis mismatch: expected " + expectedAxis
                            + " but was " + descriptor.axis());
                }
            });
            return descriptors;
        }

        public SensoryDescriptors build() {
            var allDescriptors = Stream.of(taste, aroma, mouthfeel, sensation, impression)
                    .flatMap(Set::stream)
                    .toList();
            return new SensoryDescriptors(allDescriptors);
        }
    }
}

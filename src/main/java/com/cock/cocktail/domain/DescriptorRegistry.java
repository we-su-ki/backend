package com.cock.cocktail.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Descriptor 등록소
 *
 * 감각 축별 descriptor 정의를 저장하고, 쿼리 매칭 기능을 제공합니다.
 */
public record DescriptorRegistry(
        List<DescriptorDefinition> taste,
        List<DescriptorDefinition> aroma,
        List<DescriptorDefinition> mouthfeel,
        List<DescriptorDefinition> sensation,
        List<DescriptorDefinition> impression
) {
    public DescriptorRegistry {
        if (taste == null) taste = new ArrayList<>();
        if (aroma == null) aroma = new ArrayList<>();
        if (mouthfeel == null) mouthfeel = new ArrayList<>();
        if (sensation == null) sensation = new ArrayList<>();
        if (impression == null) impression = new ArrayList<>();
    }

    public Set<DescriptorCode> findMatches(String query, SensoryAxis axis) {
        List<DescriptorDefinition> definitions = switch (axis) {
            case TASTE -> taste();
            case AROMA -> aroma();
            case MOUTHFEEL -> mouthfeel();
            case SENSATION -> sensation();
            case IMPRESSION -> impression();
        };

        return definitions.stream()
                .flatMap(definition -> definition.findMatches(query).stream())
                .map(code -> new DescriptorCode(axis, code))
                .collect(Collectors.toUnmodifiableSet());
    }
}

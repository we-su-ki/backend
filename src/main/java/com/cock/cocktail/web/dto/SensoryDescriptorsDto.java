package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.SensoryDescriptors;

import java.util.List;

/**
 * 감각 축별 descriptor 웹 응답 DTO
 */
public record SensoryDescriptorsDto(
        List<String> taste,
        List<String> aroma,
        List<String> mouthfeel,
        List<String> sensation,
        List<String> impression
) {
    public static SensoryDescriptorsDto from(SensoryDescriptors descriptors) {
        return new SensoryDescriptorsDto(
                toStringList(descriptors.taste()),
                toStringList(descriptors.aroma()),
                toStringList(descriptors.mouthfeel()),
                toStringList(descriptors.sensation()),
                toStringList(descriptors.impression())
        );
    }

    private static List<String> toStringList(java.util.Set<DescriptorCode> descriptorCodes) {
        return descriptorCodes.stream()
                .map(DescriptorCode::value)
                .toList();
    }
}

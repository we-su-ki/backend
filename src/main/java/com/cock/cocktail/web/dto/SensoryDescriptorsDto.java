package com.cock.cocktail.web.dto;

import com.cock.cocktail.domain.SensoryAxis;
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
                List.copyOf(descriptors.codeValues(SensoryAxis.TASTE)),
                List.copyOf(descriptors.codeValues(SensoryAxis.AROMA)),
                List.copyOf(descriptors.codeValues(SensoryAxis.MOUTHFEEL)),
                List.copyOf(descriptors.codeValues(SensoryAxis.SENSATION)),
                List.copyOf(descriptors.codeValues(SensoryAxis.IMPRESSION))
        );
    }
}

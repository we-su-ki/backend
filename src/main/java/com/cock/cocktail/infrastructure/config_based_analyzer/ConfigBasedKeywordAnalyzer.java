package com.cock.cocktail.infrastructure.config_based_analyzer;

import com.cock.cocktail.domain.descriptor.DescriptorRegistry;
import com.cock.cocktail.domain.descriptor.SensoryDescriptors;
import com.cock.cocktail.service.KeywordAnalyzer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.cock.cocktail.domain.descriptor.SensoryAxis.*;

@Service
@RequiredArgsConstructor
public class ConfigBasedKeywordAnalyzer implements KeywordAnalyzer {

    private final DescriptorRegistry descriptorRegistry;

    @Override
    public SensoryDescriptors analyze(String query) {
        return SensoryDescriptors.builder()
                .taste(descriptorRegistry.findMatches(query, TASTE))
                .aroma(descriptorRegistry.findMatches(query, AROMA))
                .mouthfeel(descriptorRegistry.findMatches(query, MOUTHFEEL))
                .sensation(descriptorRegistry.findMatches(query, SENSATION))
                .impression(descriptorRegistry.findMatches(query, IMPRESSION))
                .build();
    }
}

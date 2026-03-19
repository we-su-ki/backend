package com.cock.cocktail.infrastructure.config_based_analyzer;

import static com.cock.cocktail.domain.SensoryAxis.AROMA;
import static com.cock.cocktail.domain.SensoryAxis.IMPRESSION;
import static com.cock.cocktail.domain.SensoryAxis.MOUTHFEEL;
import static com.cock.cocktail.domain.SensoryAxis.SENSATION;
import static com.cock.cocktail.domain.SensoryAxis.TASTE;

import com.cock.cocktail.service.KeywordAnalyzer;
import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.SensoryAxis;
import com.cock.cocktail.domain.SensoryDescriptors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class ConfigBasedKeywordAnalyzer implements KeywordAnalyzer {

    private final DescriptorConfig descriptorConfig;

    public ConfigBasedKeywordAnalyzer() {
        this.descriptorConfig = loadDescriptorConfig();
    }

    @Override
    public SensoryDescriptors analyze(String query) {
        var tasteDescriptors = extractDescriptors(query, descriptorConfig.taste(), TASTE);
        var aromaDescriptors = extractDescriptors(query, descriptorConfig.aroma(), AROMA);
        var mouthfeelDescriptors = extractDescriptors(query, descriptorConfig.mouthfeel(), MOUTHFEEL);
        var sensationDescriptors = extractDescriptors(query, descriptorConfig.sensation(), SENSATION);
        var impressionDescriptors = extractDescriptors(query, descriptorConfig.impression(), IMPRESSION);

        return SensoryDescriptors.builder()
                .taste(tasteDescriptors)
                .aroma(aromaDescriptors)
                .mouthfeel(mouthfeelDescriptors)
                .sensation(sensationDescriptors)
                .impression(impressionDescriptors)
                .build();
    }

    private Set<DescriptorCode> extractDescriptors(
            String query,
            List<DescriptorDefinition> definitions,
            SensoryAxis axis
    ) {
        return definitions.stream()
                .flatMap(definition -> definition.findMatches(query).stream())
                .map(code -> new DescriptorCode(axis, code))
                .collect(Collectors.toUnmodifiableSet());
    }

    private DescriptorConfig loadDescriptorConfig() {
        try {
            var yamlMapper = new ObjectMapper(new YAMLFactory());
            var keywordsResource = new ClassPathResource("keywords.yml");
            return yamlMapper.readValue(keywordsResource.getInputStream(), DescriptorConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load descriptor configuration", e);
        }
    }

    private record DescriptorConfig(
            List<DescriptorDefinition> taste,
            List<DescriptorDefinition> aroma,
            List<DescriptorDefinition> mouthfeel,
            List<DescriptorDefinition> sensation,
            List<DescriptorDefinition> impression
    ) {
        private DescriptorConfig {
            if (taste == null) taste = new ArrayList<>();
            if (aroma == null) aroma = new ArrayList<>();
            if (mouthfeel == null) mouthfeel = new ArrayList<>();
            if (sensation == null) sensation = new ArrayList<>();
            if (impression == null) impression = new ArrayList<>();
        }
    }
}

package com.cock.cocktail.infrastructure.matching;

import com.cock.cocktail.domain.DescriptorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ReasonGenerator {

    private final Map<String, String> codeToLabel;

    public ReasonGenerator() {
        this.codeToLabel = loadCodeToLabelMap();
    }

    public String generate(Set<DescriptorCode> matchedDescriptors) {
        if (matchedDescriptors == null || matchedDescriptors.isEmpty()) {
            return "추천 칵테일입니다.";
        }

        var labels = matchedDescriptors.stream()
                .map(descriptor -> codeToLabel.get(descriptor.value()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (labels.isEmpty()) {
            return "추천 칵테일입니다.";
        }

        return String.join(", ", labels) + " 특징을 가진 칵테일입니다.";
    }

    private Map<String, String> loadCodeToLabelMap() {
        try {
            var yamlMapper = new ObjectMapper(new YAMLFactory());
            var keywordsResource = new ClassPathResource("keywords.yml");
            var config = yamlMapper.readValue(keywordsResource.getInputStream(), DescriptorConfig.class);

            var codeToLabel = new HashMap<String, String>();
            extractLabels(config.taste(), codeToLabel);
            extractLabels(config.aroma(), codeToLabel);
            extractLabels(config.mouthfeel(), codeToLabel);
            extractLabels(config.sensation(), codeToLabel);
            extractLabels(config.impression(), codeToLabel);

            return codeToLabel;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load descriptor labels", e);
        }
    }

    private void extractLabels(List<DescriptorDefinition> definitions, Map<String, String> codeToLabel) {
        if (definitions == null) {
            return;
        }

        for (DescriptorDefinition definition : definitions) {
            codeToLabel.put(definition.code(), definition.label());
            if (definition.children() != null && !definition.children().isEmpty()) {
                extractLabels(definition.children(), codeToLabel);
            }
        }
    }

    private record DescriptorDefinition(
            String code,
            String label,
            List<String> synonyms,
            List<DescriptorDefinition> children
    ) {
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

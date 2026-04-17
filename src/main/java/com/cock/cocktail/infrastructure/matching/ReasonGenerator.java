package com.cock.cocktail.infrastructure.matching;

import com.cock.cocktail.domain.descriptor.DescriptorCode;
import com.cock.cocktail.domain.descriptor.DescriptorDefinition;
import com.cock.cocktail.domain.descriptor.DescriptorRegistry;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ReasonGenerator {

    private final Map<String, String> codeToLabel;

    public ReasonGenerator(DescriptorRegistry registry) {
        this.codeToLabel = buildCodeToLabelMap(registry);
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

    private Map<String, String> buildCodeToLabelMap(DescriptorRegistry registry) {
        var codeToLabel = new HashMap<String, String>();
        extractLabels(registry.taste(), codeToLabel);
        extractLabels(registry.aroma(), codeToLabel);
        extractLabels(registry.mouthfeel(), codeToLabel);
        extractLabels(registry.sensation(), codeToLabel);
        extractLabels(registry.impression(), codeToLabel);
        return codeToLabel;
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
}

package com.cock.cocktail.infrastructure.config_based_analyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * Descriptor 정의
 *
 * @param code 고유 코드 (예: "sweet", "lime")
 * @param label 표시 레이블 (예: "달달한", "라임")
 * @param synonyms 동의어 목록
 * @param children 하위 descriptor (계층 구조)
 */
record DescriptorDefinition(
        String code,
        String label,
        List<String> synonyms,
        List<DescriptorDefinition> children
) {
    public DescriptorDefinition {
        if (synonyms == null) {
            synonyms = new ArrayList<>();
        }
        if (children == null) {
            children = new ArrayList<>();
        }
    }

    public boolean matches(String query) {
        var lowerCaseQuery = query.toLowerCase();
        return synonyms.stream()
                .anyMatch(synonym -> lowerCaseQuery.contains(synonym.toLowerCase()));
    }

    /**
     * 본인 또는 자식 descriptor가 매칭되는지 확인 (재귀)
     */
    public List<String> findMatches(String query) {
        var matchedCodes = new ArrayList<String>();

        if (matches(query)) {
            matchedCodes.add(code);
        }

        for (DescriptorDefinition child : children) {
            matchedCodes.addAll(child.findMatches(query));
        }

        return matchedCodes;
    }
}

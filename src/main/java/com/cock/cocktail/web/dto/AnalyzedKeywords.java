package com.cock.cocktail.web.dto;

import java.util.List;

public record AnalyzedKeywords(
        List<String> taste,
        List<String> texture,
        List<String> carbonation,
        List<String> flavor,
        List<String> mood
) {
}

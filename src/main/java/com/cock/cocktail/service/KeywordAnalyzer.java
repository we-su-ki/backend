package com.cock.cocktail.service;

import com.cock.cocktail.domain.SensoryDescriptors;

/**
 * 사용자 입력을 감각 descriptor로 분석하는 포트.
 */
public interface KeywordAnalyzer {

    SensoryDescriptors analyze(String query);
}

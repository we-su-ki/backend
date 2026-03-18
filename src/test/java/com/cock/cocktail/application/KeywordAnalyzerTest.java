package com.cock.cocktail.application;

import com.cock.cocktail.service.KeywordAnalyzer;
import com.cock.cocktail.domain.SensoryAxis;
import com.cock.cocktail.infrastructure.config_based_analyzer.ConfigBasedKeywordAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KeywordAnalyzerTest {

    private KeywordAnalyzer keywordAnalyzer;

    @BeforeEach
    void setUp() {
        keywordAnalyzer = new ConfigBasedKeywordAnalyzer();
    }

    @Test
    @DisplayName("맛 키워드 추출")
    void shouldExtractTasteKeywords() {
        // given
        var userQuery = "달달한 칵테일 추천해줘";

        // when
        var sensoryDescriptors = keywordAnalyzer.analyze(userQuery);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.TASTE)).contains("sweet");
        assertThat(sensoryDescriptors.mouthfeel()).isEmpty();
        assertThat(sensoryDescriptors.sensation()).isEmpty();
        assertThat(sensoryDescriptors.aroma()).isEmpty();
        assertThat(sensoryDescriptors.impression()).isEmpty();
    }

    @Test
    @DisplayName("질감 키워드 추출")
    void shouldExtractTextureKeywords() {
        // given
        var userQuery = "부드러운 칵테일이 좋아";

        // when
        var sensoryDescriptors = keywordAnalyzer.analyze(userQuery);

        // then
        assertThat(sensoryDescriptors.taste()).isEmpty();
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.MOUTHFEEL)).contains("smooth");
        assertThat(sensoryDescriptors.sensation()).isEmpty();
        assertThat(sensoryDescriptors.aroma()).isEmpty();
        assertThat(sensoryDescriptors.impression()).isEmpty();
    }

    @Test
    @DisplayName("자극감(탄산) 키워드 추출")
    void shouldExtractCarbonationKeywords() {
        // given
        var userQuery = "톡 쏘는 칵테일 원해";

        // when
        var sensoryDescriptors = keywordAnalyzer.analyze(userQuery);

        // then
        assertThat(sensoryDescriptors.taste()).isEmpty();
        assertThat(sensoryDescriptors.mouthfeel()).isEmpty();
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.SENSATION)).contains("carbonated");
        assertThat(sensoryDescriptors.aroma()).isEmpty();
        assertThat(sensoryDescriptors.impression()).isEmpty();
    }

    @Test
    @DisplayName("향 키워드 추출")
    void shouldExtractFlavorKeywords() {
        // given
        var userQuery = "과일향 나는 칵테일";

        // when
        var sensoryDescriptors = keywordAnalyzer.analyze(userQuery);

        // then
        assertThat(sensoryDescriptors.taste()).isEmpty();
        assertThat(sensoryDescriptors.mouthfeel()).isEmpty();
        assertThat(sensoryDescriptors.sensation()).isEmpty();
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.AROMA)).contains("fruity");
        assertThat(sensoryDescriptors.impression()).isEmpty();
    }

    @Test
    @DisplayName("분위기 키워드 추출")
    void shouldExtractMoodKeywords() {
        // given
        var userQuery = "여름에 어울리는 칵테일";

        // when
        var sensoryDescriptors = keywordAnalyzer.analyze(userQuery);

        // then
        assertThat(sensoryDescriptors.taste()).isEmpty();
        assertThat(sensoryDescriptors.mouthfeel()).isEmpty();
        assertThat(sensoryDescriptors.sensation()).isEmpty();
        assertThat(sensoryDescriptors.aroma()).isEmpty();
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.IMPRESSION)).contains("summer");
    }

    @Test
    @DisplayName("복합 키워드 추출")
    void shouldExtractMultipleKeywords() {
        // given
        var userQuery = "달달하고 톡 쏘는 과일향이 나는 부드러운 칵테일 추천해줘";

        // when
        var sensoryDescriptors = keywordAnalyzer.analyze(userQuery);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.TASTE)).contains("sweet");
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.MOUTHFEEL)).contains("smooth");
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.SENSATION)).contains("carbonated");
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.AROMA)).contains("fruity");
        assertThat(sensoryDescriptors.impression()).isEmpty();
    }

    @Test
    @DisplayName("동의어 처리 - 달콤한은 sweet으로 변환")
    void shouldHandleSynonyms() {
        // given
        var userQuery = "달콤한 칵테일";

        // when
        var sensoryDescriptors = keywordAnalyzer.analyze(userQuery);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.TASTE)).contains("sweet");
    }

    @Test
    @DisplayName("동의어 처리 - 새콤달콤한")
    void shouldHandleMultipleSynonyms() {
        // given
        var userQuery = "새콤달콤한 칵테일";

        // when
        var sensoryDescriptors = keywordAnalyzer.analyze(userQuery);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.TASTE)).containsExactlyInAnyOrder("sour", "sweet");
    }

    @Test
    @DisplayName("키워드가 없는 경우 빈 리스트 반환")
    void shouldReturnEmptyListsWhenNoKeywordsFound() {
        // given
        var userQuery = "칵테일 추천해줘";

        // when
        var sensoryDescriptors = keywordAnalyzer.analyze(userQuery);

        // then
        assertThat(sensoryDescriptors.taste()).isEmpty();
        assertThat(sensoryDescriptors.mouthfeel()).isEmpty();
        assertThat(sensoryDescriptors.sensation()).isEmpty();
        assertThat(sensoryDescriptors.aroma()).isEmpty();
        assertThat(sensoryDescriptors.impression()).isEmpty();
    }

    @Test
    @DisplayName("여러 맛 키워드 추출")
    void shouldExtractMultipleTasteKeywords() {
        // given
        var userQuery = "달달하면서 새콤한 칵테일";

        // when
        var sensoryDescriptors = keywordAnalyzer.analyze(userQuery);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.TASTE)).containsExactlyInAnyOrder("sweet", "sour");
    }

    @Test
    @DisplayName("대소문자 구분 없이 키워드 추출")
    void shouldBeCaseInsensitive() {
        // given
        var userQuery = "달달한 칵테일";

        // when
        var sensoryDescriptors = keywordAnalyzer.analyze(userQuery);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.TASTE)).contains("sweet");
    }

    @Test
    @DisplayName("계층적 descriptor 추출 - 라임")
    void shouldExtractHierarchicalDescriptors() {
        // given
        var userQuery = "라임 향이 나는 칵테일";

        // when
        var sensoryDescriptors = keywordAnalyzer.analyze(userQuery);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.AROMA)).contains("lime");
    }

    @Test
    @DisplayName("계층적 descriptor 추출 - 시트러스 언급시 상위 카테고리만 추출")
    void shouldExtractParentCategory() {
        // given
        var userQuery = "시트러스 향이 나는 칵테일";

        // when
        var sensoryDescriptors = keywordAnalyzer.analyze(userQuery);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.AROMA)).contains("citrus");
    }

    @Test
    @DisplayName("계층적 descriptor 추출 - 과일향 언급시 최상위 카테고리 추출")
    void shouldExtractTopLevelCategory() {
        // given
        var userQuery = "과일향 나는 칵테일";

        // when
        var sensoryDescriptors = keywordAnalyzer.analyze(userQuery);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.AROMA)).contains("fruity");
    }
}

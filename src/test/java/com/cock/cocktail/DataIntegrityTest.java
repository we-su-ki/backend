package com.cock.cocktail;

import com.cock.cocktail.domain.descriptor.SensoryAxis;
import com.cock.cocktail.service.cocktail.CocktailRecommendationService;
import com.cock.cocktail.service.KeywordAnalyzer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class DataIntegrityTest {

    @Autowired
    private KeywordAnalyzer keywordAnalyzer;

    @Autowired
    private CocktailRecommendationService recommendationService;

    @Test
    @DisplayName("전체 플로우: 사용자 입력 → 키워드 분석 → 칵테일 매칭 → 응답 생성")
    void shouldMaintainDataIntegrityThroughEntirePipeline() {
        String userInput = "달달하고 과일향 나는 부드러운 칵테일";

        var descriptors = keywordAnalyzer.analyze(userInput);

        assertAll(
                () -> assertThat(descriptors.taste()).hasSize(1),
                () -> assertThat(descriptors.taste()).anyMatch(d -> d.value().equals("sweet")),
                () -> assertThat(descriptors.aroma()).hasSize(1),
                () -> assertThat(descriptors.aroma()).anyMatch(d -> d.value().equals("fruity")),
                () -> assertThat(descriptors.mouthfeel()).hasSize(1),
                () -> assertThat(descriptors.mouthfeel()).anyMatch(d -> d.value().equals("smooth"))
        );

        var matches = recommendationService.recommend(userInput);

        assertAll(
                () -> assertThat(matches).isNotEmpty(),
                () -> assertThat(matches).hasSizeLessThanOrEqualTo(3),
                () -> assertThat(matches).allMatch(m -> m.score() > 0),
                () -> assertThat(matches).allMatch(m -> m.reason() != null),
                () -> assertThat(matches).allMatch(m -> !m.matchedDescriptors().isEmpty())
        );

        var firstMatch = matches.get(0);
        assertAll(
                () -> assertThat(firstMatch.score()).isEqualTo(1.0),
                () -> assertThat(firstMatch.reason()).contains("달달한"),
                () -> assertThat(firstMatch.reason()).contains("과일향"),
                () -> assertThat(firstMatch.reason()).contains("부드러운"),
                () -> assertThat(firstMatch.matchedDescriptors()).hasSize(3),
                () -> assertThat(firstMatch.matchedDescriptors())
                        .anyMatch(d -> d.axis() == SensoryAxis.TASTE && d.value().equals("sweet")),
                () -> assertThat(firstMatch.matchedDescriptors())
                        .anyMatch(d -> d.axis() == SensoryAxis.AROMA && d.value().equals("fruity")),
                () -> assertThat(firstMatch.matchedDescriptors())
                        .anyMatch(d -> d.axis() == SensoryAxis.MOUTHFEEL && d.value().equals("smooth"))
        );
    }

    @Test
    @DisplayName("키워드 분석 정확성: 단일 키워드")
    void shouldAnalyzeKeywordsAccurately_SingleKeyword() {
        var descriptors = keywordAnalyzer.analyze("달달한 칵테일");

        assertAll(
                () -> assertThat(descriptors.taste()).hasSize(1),
                () -> assertThat(descriptors.taste()).anyMatch(d -> d.value().equals("sweet")),
                () -> assertThat(descriptors.aroma()).isEmpty(),
                () -> assertThat(descriptors.mouthfeel()).isEmpty()
        );
    }

    @Test
    @DisplayName("키워드 분석 정확성: 복합 키워드")
    void shouldAnalyzeKeywordsAccurately_MultipleKeywords() {
        var descriptors = keywordAnalyzer.analyze("달달하고 톡 쏘는 칵테일");

        assertAll(
                () -> assertThat(descriptors.taste()).anyMatch(d -> d.value().equals("sweet")),
                () -> assertThat(descriptors.sensation()).anyMatch(d -> d.value().equals("carbonated"))
        );
    }

    @Test
    @DisplayName("매칭 점수 정확성: 모든 키워드 매칭")
    void shouldCalculateScoreAccurately_AllKeywordsMatch() {
        var matches = recommendationService.recommend("달달하고 과일향 나는 부드러운 칵테일");

        assertAll(
                () -> assertThat(matches).isNotEmpty(),
                () -> assertThat(matches.get(0).score()).isEqualTo(1.0),
                () -> assertThat(matches.get(0).matchedDescriptors()).hasSize(3)
        );
    }

    @Test
    @DisplayName("추천 이유 정확성: 매칭된 키워드가 이유에 포함")
    void shouldGenerateReasonAccurately() {
        var matches = recommendationService.recommend("달달한 칵테일");

        assertAll(
                () -> assertThat(matches).isNotEmpty(),
                () -> assertThat(matches.get(0).reason()).contains("달달한")
        );
    }

    @Test
    @DisplayName("매칭 키워드 정확성: 응답에 descriptor code 포함")
    void shouldIncludeMatchedKeywordsAccurately() {
        var matches = recommendationService.recommend("달달하고 과일향 나는 칵테일");

        var firstMatch = matches.get(0);
        var matchedDescriptorValues = firstMatch.matchedDescriptors().stream()
                .map(d -> d.value())
                .toList();

        assertAll(
                () -> assertThat(matchedDescriptorValues).contains("sweet"),
                () -> assertThat(matchedDescriptorValues).contains("fruity")
        );
    }

    @Test
    @DisplayName("정렬 정확성: 점수 내림차순")
    void shouldSortByScoreDescending() {
        var matches = recommendationService.recommend("달달한 칵테일");

        for (int i = 0; i < matches.size() - 1; i++) {
            assertThat(matches.get(i).score())
                    .isGreaterThanOrEqualTo(matches.get(i + 1).score());
        }
    }

    @Test
    @DisplayName("최대 개수 제한: 3개 이하 반환")
    void shouldReturnMaximumThreeCocktails() {
        var matches = recommendationService.recommend("달달한");

        assertThat(matches).hasSizeLessThanOrEqualTo(3);
    }
}

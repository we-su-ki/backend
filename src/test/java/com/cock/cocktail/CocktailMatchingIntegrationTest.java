package com.cock.cocktail;

import com.cock.cocktail.service.cocktail.CocktailMatcher;
import com.cock.cocktail.service.KeywordAnalyzer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * 칵테일 매칭 통합 테스트
 * <p>
 * KeywordAnalyzer → CocktailMatcher 전체 플로우 검증
 */
@SpringBootTest
class CocktailMatchingIntegrationTest {

    @Autowired
    private KeywordAnalyzer keywordAnalyzer;

    @Autowired
    private CocktailMatcher cocktailMatcher;

    @Test
    @DisplayName("달달하고 과일향 나는 칵테일 - 매칭 성공")
    void shouldMatchSweetAndFruityCocktails() {
        var query = "달달하고 과일향 나는 칵테일";

        var descriptors = keywordAnalyzer.analyze(query);
        var matches = cocktailMatcher.match(descriptors);

        assertAll(
                () -> assertThat(descriptors.taste()).isNotEmpty(),
                () -> assertThat(descriptors.aroma()).isNotEmpty(),
                () -> assertThat(matches).isNotEmpty(),
                () -> assertThat(matches).allMatch(match -> match.score() > 0.0),
                () -> assertThat(matches).hasSizeLessThanOrEqualTo(3)
        );
    }

    @Test
    @DisplayName("부드럽고 톡 쏘는 칵테일 - 매칭 성공")
    void shouldMatchSmoothAndCarbonatedCocktails() {
        var query = "부드럽고 톡 쏘는 칵테일";

        var descriptors = keywordAnalyzer.analyze(query);
        var matches = cocktailMatcher.match(descriptors);

        assertAll(
                () -> assertThat(descriptors.mouthfeel()).isNotEmpty(),
                () -> assertThat(descriptors.sensation()).isNotEmpty(),
                () -> assertThat(matches).isNotEmpty(),
                () -> assertThat(matches).allMatch(match -> match.score() > 0.0)
        );
    }

    @Test
    @DisplayName("존재하지 않는 키워드 - 빈 리스트")
    void shouldReturnEmptyListForNonexistentKeywords() {
        var query = "완전히존재하지않는키워드칵테일";

        var descriptors = keywordAnalyzer.analyze(query);
        var matches = cocktailMatcher.match(descriptors);

        assertAll(
                () -> assertThat(descriptors.isEmpty()).isTrue(),
                () -> assertThat(matches).isEmpty()
        );
    }

    @Test
    @DisplayName("매칭 점수 정렬 확인")
    void shouldSortMatchesByScoreDescending() {
        var query = "달달하고 과일향 나는 부드러운 칵테일";

        var descriptors = keywordAnalyzer.analyze(query);
        var matches = cocktailMatcher.match(descriptors);

        if (matches.size() > 1) {
            for (int i = 0; i < matches.size() - 1; i++) {
                assertThat(matches.get(i).score())
                        .isGreaterThanOrEqualTo(matches.get(i + 1).score());
            }
        }
    }

    @Test
    @DisplayName("추천 이유 생성 확인")
    void shouldGenerateReasons() {
        var query = "달달하고 과일향 나는 칵테일";

        var descriptors = keywordAnalyzer.analyze(query);
        var matches = cocktailMatcher.match(descriptors);

        assertAll(
                () -> assertThat(matches).isNotEmpty(),
                () -> assertThat(matches).allMatch(match -> match.reason() != null),
                () -> assertThat(matches).allMatch(match -> !match.reason().isBlank())
        );
    }

    @Test
    @DisplayName("전체 플로우 - 자연어 쿼리부터 추천까지")
    void shouldCompleteFullFlow() {
        var query = "새콤달콤한 여름에 어울리는 칵테일";

        var descriptors = keywordAnalyzer.analyze(query);
        var matches = cocktailMatcher.match(descriptors);

        assertAll(
                // KeywordAnalyzer 검증
                () -> assertThat(descriptors.isEmpty()).isFalse(),

                // CocktailMatcher 검증
                () -> assertThat(matches).isNotEmpty(),
                () -> assertThat(matches).hasSizeLessThanOrEqualTo(3),

                // MatchedCocktail 검증
                () -> assertThat(matches).allMatch(match -> match.cocktail() != null),
                () -> assertThat(matches).allMatch(match -> match.score() > 0.0),
                () -> assertThat(matches).allMatch(match -> match.score() <= 1.0),
                () -> assertThat(matches).allMatch(match -> match.reason() != null),
                () -> assertThat(matches).allMatch(match -> match.matchedDescriptors() != null)
        );
    }

    @Test
    @DisplayName("점수가 높은 칵테일이 상위에 위치")
    void shouldRankHighScoreCocktailsFirst() {
        var query = "달달한";

        var descriptors = keywordAnalyzer.analyze(query);
        var matches = cocktailMatcher.match(descriptors);

        if (matches.size() >= 2) {
            // 첫 번째 칵테일의 점수가 마지막 칵테일보다 크거나 같아야 함
            assertThat(matches.get(0).score())
                    .isGreaterThanOrEqualTo(matches.get(matches.size() - 1).score());
        }
    }

    @Test
    @DisplayName("매칭된 descriptors가 쿼리의 부분집합인지 확인")
    void shouldMatchedDescriptorsBeSubsetOfQuery() {
        var query = "달달하고 과일향 나는 칵테일";

        var descriptors = keywordAnalyzer.analyze(query);
        var matches = cocktailMatcher.match(descriptors);

        if (!matches.isEmpty()) {
            var queryDescriptors = extractAll(descriptors);
            for (var match : matches) {
                var matchedDescriptors = match.matchedDescriptors();
                // 매칭된 descriptors는 쿼리의 부분집합이어야 함
                assertThat(queryDescriptors).containsAll(matchedDescriptors);
            }
        }
    }

    private java.util.Set<com.cock.cocktail.domain.descriptor.DescriptorCode> extractAll(
            com.cock.cocktail.domain.descriptor.SensoryDescriptors descriptors) {
        return java.util.stream.Stream.of(
                descriptors.taste(),
                descriptors.aroma(),
                descriptors.mouthfeel(),
                descriptors.sensation(),
                descriptors.impression()
        ).flatMap(java.util.Set::stream).collect(java.util.stream.Collectors.toSet());
    }
}

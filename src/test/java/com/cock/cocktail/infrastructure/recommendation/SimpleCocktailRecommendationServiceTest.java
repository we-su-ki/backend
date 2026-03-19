package com.cock.cocktail.infrastructure.recommendation;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.MatchedCocktail;
import com.cock.cocktail.domain.SensoryAxis;
import com.cock.cocktail.domain.SensoryDescriptors;
import com.cock.cocktail.service.CocktailMatcher;
import com.cock.cocktail.service.KeywordAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleCocktailRecommendationServiceTest {

    @Mock
    private KeywordAnalyzer keywordAnalyzer;

    @Mock
    private CocktailMatcher cocktailMatcher;

    private SimpleCocktailRecommendationService service;

    @BeforeEach
    void setUp() {
        service = new SimpleCocktailRecommendationService(keywordAnalyzer, cocktailMatcher);
    }

    @Test
    @DisplayName("정상적인 추천 요청 - 결과 반환")
    void shouldRecommendCocktailsSuccessfully() {
        var query = "달달하고 과일향 나는 칵테일";
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .build();

        var mockCocktail = Cocktail.builder()
                .id(1L)
                .name("Mojito")
                .recipe("Recipe")
                .sensoryDescriptors(List.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.AROMA, "fruity")
                ))
                .build();

        var matchedCocktail = new MatchedCocktail(
                mockCocktail,
                0.85,
                "달달한, 과일향 특징을 가진 칵테일입니다.",
                Set.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.AROMA, "fruity")
                )
        );

        when(keywordAnalyzer.analyze(query)).thenReturn(descriptors);
        when(cocktailMatcher.match(descriptors)).thenReturn(List.of(matchedCocktail));

        var result = service.recommend(query);

        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result.get(0).getCocktail().getName()).isEqualTo("Mojito"),
                () -> assertThat(result.get(0).getScore()).isEqualTo(0.85)
        );
    }

    @Test
    @DisplayName("빈 문자열 입력 - 예외 발생")
    void shouldThrowExceptionForBlankQuery() {
        assertThatThrownBy(() -> service.recommend(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("query must not be blank");
    }

    @Test
    @DisplayName("null 입력 - 예외 발생")
    void shouldThrowExceptionForNullQuery() {
        assertThatThrownBy(() -> service.recommend(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("query must not be blank");
    }

    @Test
    @DisplayName("키워드 분석 실패 시 빈 리스트 반환")
    void shouldReturnEmptyListWhenNoKeywordsFound() {
        var query = "칵테일 추천해줘";
        var emptyDescriptors = SensoryDescriptors.builder().build();

        when(keywordAnalyzer.analyze(query)).thenReturn(emptyDescriptors);
        when(cocktailMatcher.match(emptyDescriptors)).thenReturn(List.of());

        var result = service.recommend(query);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("매칭 결과 없을 때 빈 리스트 반환")
    void shouldReturnEmptyListWhenNoMatchesFound() {
        var query = "완전히존재하지않는맛";
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "nonexistent")))
                .build();

        when(keywordAnalyzer.analyze(query)).thenReturn(descriptors);
        when(cocktailMatcher.match(descriptors)).thenReturn(List.of());

        var result = service.recommend(query);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("추천 결과 개수 확인 - CocktailMatcher가 반환한 개수 그대로")
    void shouldReturnMatcherResults() {
        var query = "달달한 칵테일";
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .build();

        // CocktailMatcher는 이미 최대 3개로 제한하여 반환
        var matches = List.of(
                createMatchedCocktail(1L, "Cocktail1", 0.9),
                createMatchedCocktail(2L, "Cocktail2", 0.8),
                createMatchedCocktail(3L, "Cocktail3", 0.7)
        );

        when(keywordAnalyzer.analyze(query)).thenReturn(descriptors);
        when(cocktailMatcher.match(descriptors)).thenReturn(matches);

        var result = service.recommend(query);

        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("CocktailMatcher의 정렬 결과 유지")
    void shouldPreserveMatcherSortOrder() {
        var query = "달달한 칵테일";
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .build();

        // CocktailMatcher가 이미 정렬해서 반환
        var matches = List.of(
                createMatchedCocktail(1L, "Cocktail1", 0.9),
                createMatchedCocktail(2L, "Cocktail2", 0.7),
                createMatchedCocktail(3L, "Cocktail3", 0.5)
        );

        when(keywordAnalyzer.analyze(query)).thenReturn(descriptors);
        when(cocktailMatcher.match(descriptors)).thenReturn(matches);

        var result = service.recommend(query);

        // then - 순서 유지 확인
        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> assertThat(result.get(0).getScore()).isEqualTo(0.9),
                () -> assertThat(result.get(1).getScore()).isEqualTo(0.7),
                () -> assertThat(result.get(2).getScore()).isEqualTo(0.5)
        );
    }

    private MatchedCocktail createMatchedCocktail(Long id, String name, double score) {
        var cocktail = Cocktail.builder()
                .id(id)
                .name(name)
                .recipe("Recipe")
                .sensoryDescriptors(List.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .build();

        return new MatchedCocktail(
                cocktail,
                score,
                "Test reason",
                Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet"))
        );
    }
}

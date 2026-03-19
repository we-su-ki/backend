package com.cock.cocktail.infrastructure.matching;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.SensoryAxis;
import com.cock.cocktail.domain.SensoryDescriptors;
import com.cock.cocktail.repository.CocktailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 칵테일 매칭 엣지 케이스 테스트
 */
@ExtendWith(MockitoExtension.class)
class EdgeCaseTest {

    @Mock
    private CocktailRepository cocktailRepository;

    private SimpleCocktailMatcher cocktailMatcher;

    @BeforeEach
    void setUp() {
        var scoreCalculator = new MatchScoreCalculator();
        var reasonGenerator = new ReasonGenerator();
        cocktailMatcher = new SimpleCocktailMatcher(cocktailRepository, scoreCalculator, reasonGenerator);
    }

    @Test
    @DisplayName("쿼리 descriptor가 빈 경우 - 빈 리스트 반환")
    void shouldReturnEmptyListForEmptyQuery() {
        // given
        var emptyDescriptors = SensoryDescriptors.builder().build();

        // when
        var matches = cocktailMatcher.match(emptyDescriptors);

        // then
        assertThat(matches).isEmpty();
    }

    @Test
    @DisplayName("모든 칵테일 점수 0인 경우 - 빈 리스트 반환")
    void shouldReturnEmptyListWhenAllScoresAreZero() {
        // given - 존재하지 않는 descriptor
        var nonexistentDescriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "nonexistent123")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "nonexistent456")))
                .build();

        // when
        var matches = cocktailMatcher.match(nonexistentDescriptors);

        // then
        assertThat(matches).isEmpty();
    }

    @Test
    @DisplayName("매우 긴 쿼리 처리")
    void shouldHandleVeryLongQuery() {
        // given - 모든 축에 descriptor 포함
        var longDescriptors = SensoryDescriptors.builder()
                .taste(Set.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.TASTE, "sour"),
                        new DescriptorCode(SensoryAxis.TASTE, "bitter")
                ))
                .aroma(Set.of(
                        new DescriptorCode(SensoryAxis.AROMA, "fruity"),
                        new DescriptorCode(SensoryAxis.AROMA, "mint")
                ))
                .mouthfeel(Set.of(
                        new DescriptorCode(SensoryAxis.MOUTHFEEL, "smooth")
                ))
                .sensation(Set.of(
                        new DescriptorCode(SensoryAxis.SENSATION, "carbonated")
                ))
                .impression(Set.of(
                        new DescriptorCode(SensoryAxis.IMPRESSION, "summer"),
                        new DescriptorCode(SensoryAxis.IMPRESSION, "refreshing")
                ))
                .build();

        var mockCocktail = Cocktail.builder()
                .id(1L)
                .name("Complex Cocktail")
                .recipe("Recipe")
                .sensoryDescriptors(List.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.AROMA, "fruity"),
                        new DescriptorCode(SensoryAxis.IMPRESSION, "summer")
                ))
                .build();

        when(cocktailRepository.findByDescriptors(any())).thenReturn(List.of(mockCocktail));

        // when
        var matches = cocktailMatcher.match(longDescriptors);

        // then
        assertAll(
                () -> assertThat(matches).isNotNull(),
                () -> assertThat(matches).hasSizeLessThanOrEqualTo(3),
                () -> assertThat(matches).allMatch(match -> match.getScore() > 0.0)
        );
    }

    @Test
    @DisplayName("반복 호출 시 동일한 결과 반환 (멱등성)")
    void shouldReturnSameResultsForMultipleCalls() {
        // given
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .build();

        var mockCocktail = Cocktail.builder()
                .id(1L)
                .name("Sweet Cocktail")
                .recipe("Recipe")
                .sensoryDescriptors(List.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .build();

        when(cocktailRepository.findByDescriptors(any())).thenReturn(List.of(mockCocktail));

        // when
        var firstCall = cocktailMatcher.match(descriptors);
        var secondCall = cocktailMatcher.match(descriptors);

        // then
        assertAll(
                () -> assertThat(firstCall).hasSameSizeAs(secondCall),
                () -> {
                    if (!firstCall.isEmpty() && !secondCall.isEmpty()) {
                        for (int i = 0; i < firstCall.size(); i++) {
                            assertThat(firstCall.get(i).getCocktail().getId())
                                    .isEqualTo(secondCall.get(i).getCocktail().getId());
                            assertThat(firstCall.get(i).getScore())
                                    .isEqualTo(secondCall.get(i).getScore());
                        }
                    }
                }
        );
    }

    @Test
    @DisplayName("점수 경계값 확인 - 0.0 ~ 1.0 범위")
    void shouldScoreBeWithinValidRange() {
        // given
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .build();

        var mockCocktail = Cocktail.builder()
                .id(1L)
                .name("Test Cocktail")
                .recipe("Recipe")
                .sensoryDescriptors(List.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                        new DescriptorCode(SensoryAxis.AROMA, "fruity")
                ))
                .build();

        when(cocktailRepository.findByDescriptors(any())).thenReturn(List.of(mockCocktail));

        // when
        var matches = cocktailMatcher.match(descriptors);

        // then
        assertThat(matches).allMatch(match -> {
            double score = match.getScore();
            return score >= 0.0 && score <= 1.0;
        });
    }
}

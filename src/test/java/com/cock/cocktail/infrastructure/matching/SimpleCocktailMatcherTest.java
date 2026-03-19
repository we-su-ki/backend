package com.cock.cocktail.infrastructure.matching;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.SensoryAxis;
import com.cock.cocktail.domain.SensoryDescriptors;
import com.cock.cocktail.repository.CocktailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class SimpleCocktailMatcherTest {

    @Autowired
    private SimpleCocktailMatcher matcher;

    @Autowired
    private CocktailRepository cocktailRepository;

    @Test
    @DisplayName("단일 descriptor 매칭 테스트")
    void shouldMatchCocktailsWithSingleDescriptor() {
        // given
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .build();

        // when
        var matches = matcher.match(descriptors);

        // then
        assertAll(
                () -> assertThat(matches).isNotEmpty(),
                () -> assertThat(matches).allMatch(match -> match.getScore() > 0.0),
                () -> assertThat(matches).allMatch(match -> match.getCocktail() != null),
                () -> assertThat(matches).allMatch(match -> match.getReason() != null)
        );
    }

    @Test
    @DisplayName("복수 descriptor 매칭 테스트")
    void shouldMatchCocktailsWithMultipleDescriptors() {
        // given
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .build();

        // when
        var matches = matcher.match(descriptors);

        // then
        assertAll(
                () -> assertThat(matches).isNotEmpty(),
                () -> assertThat(matches).allMatch(match -> match.getScore() > 0.0)
        );
    }

    @Test
    @DisplayName("상위 N개 선정 테스트 - 최대 3개")
    void shouldReturnTopThreeMatches() {
        // given
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .build();

        // when
        var matches = matcher.match(descriptors);

        // then
        assertThat(matches).hasSizeLessThanOrEqualTo(3);
    }

    @Test
    @DisplayName("매칭되는 칵테일 없으면 빈 리스트")
    void shouldReturnEmptyListWhenNoMatch() {
        // given - 존재하지 않는 descriptor
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "nonexistent")))
                .build();

        // when
        var matches = matcher.match(descriptors);

        // then
        assertThat(matches).isEmpty();
    }

    @Test
    @DisplayName("점수 내림차순 정렬 테스트")
    void shouldSortByScoreDescending() {
        // given
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .build();

        // when
        var matches = matcher.match(descriptors);

        // then
        if (matches.size() > 1) {
            for (int i = 0; i < matches.size() - 1; i++) {
                assertThat(matches.get(i).getScore())
                        .isGreaterThanOrEqualTo(matches.get(i + 1).getScore());
            }
        }
    }

    @Test
    @DisplayName("점수 0인 칵테일 제외 테스트")
    void shouldExcludeZeroScoreCocktails() {
        // given
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .build();

        // when
        var matches = matcher.match(descriptors);

        // then
        assertThat(matches).allMatch(match -> match.getScore() > 0.0);
    }

    @Test
    @DisplayName("빈 descriptors로 검색 시 빈 리스트 반환")
    void shouldReturnEmptyListForEmptyDescriptors() {
        // given
        var descriptors = SensoryDescriptors.builder().build();

        // when
        var matches = matcher.match(descriptors);

        // then
        assertThat(matches).isEmpty();
    }

    @Test
    @DisplayName("매칭된 descriptors 포함 여부 확인")
    void shouldIncludeMatchedDescriptors() {
        // given
        var sweetDescriptor = new DescriptorCode(SensoryAxis.TASTE, "sweet");
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(sweetDescriptor))
                .build();

        // when
        var matches = matcher.match(descriptors);

        // then
        if (!matches.isEmpty()) {
            assertThat(matches.get(0).getMatchedDescriptors()).isNotNull();
        }
    }
}

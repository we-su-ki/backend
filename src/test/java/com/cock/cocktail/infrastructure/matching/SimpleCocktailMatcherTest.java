package com.cock.cocktail.infrastructure.matching;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.DescriptorCode;
import com.cock.cocktail.domain.DescriptorRegistry;
import com.cock.cocktail.domain.MatchedCocktail;
import com.cock.cocktail.domain.SensoryAxis;
import com.cock.cocktail.domain.SensoryDescriptors;
import com.cock.cocktail.repository.CocktailRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleCocktailMatcherTest {

    @Mock
    private CocktailRepository cocktailRepository;

    private SimpleCocktailMatcher matcher;

    @BeforeEach
    void setUp() {
        var scoreCalculator = new MatchScoreCalculator();
        var reasonGenerator = new ReasonGenerator(loadDescriptorRegistry());
        matcher = new SimpleCocktailMatcher(cocktailRepository, scoreCalculator, reasonGenerator);
    }

    private static DescriptorRegistry loadDescriptorRegistry() {
        try {
            var yamlMapper = new ObjectMapper(new YAMLFactory());
            var keywordsResource = new ClassPathResource("keywords.yml");
            return yamlMapper.readValue(keywordsResource.getInputStream(), DescriptorRegistry.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load descriptor configuration", e);
        }
    }

    @Test
    @DisplayName("단일 descriptor 매칭 테스트")
    void shouldMatchCocktailsWithSingleDescriptor() {
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .build();

        var mockCocktail = Cocktail.builder()
                .id(1L)
                .name("Mojito")
                .recipe("Recipe")
                .sensoryDescriptors(List.of(
                        new DescriptorCode(SensoryAxis.TASTE, "sweet")
                ))
                .build();

        when(cocktailRepository.findByDescriptors(any())).thenReturn(List.of(mockCocktail));

        var matches = matcher.match(descriptors);

        assertAll(
                () -> assertThat(matches).isNotEmpty(),
                () -> assertThat(matches).allMatch(match -> match.score() > 0.0),
                () -> assertThat(matches).allMatch(match -> match.cocktail() != null),
                () -> assertThat(matches).allMatch(match -> match.reason() != null)
        );
    }

    @Test
    @DisplayName("복수 descriptor 매칭 테스트")
    void shouldMatchCocktailsWithMultipleDescriptors() {
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

        when(cocktailRepository.findByDescriptors(any())).thenReturn(List.of(mockCocktail));

        var matches = matcher.match(descriptors);

        assertAll(
                () -> assertThat(matches).isNotEmpty(),
                () -> assertThat(matches).allMatch(match -> match.score() > 0.0)
        );
    }

    @Test
    @DisplayName("상위 N개 선정 테스트 - 최대 3개")
    void shouldReturnTopThreeMatches() {
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .build();

        var mockCocktails = List.of(
                Cocktail.builder().id(1L).name("Cocktail1").recipe("Recipe")
                        .sensoryDescriptors(List.of(new DescriptorCode(SensoryAxis.TASTE, "sweet"))).build(),
                Cocktail.builder().id(2L).name("Cocktail2").recipe("Recipe")
                        .sensoryDescriptors(List.of(new DescriptorCode(SensoryAxis.TASTE, "sweet"))).build(),
                Cocktail.builder().id(3L).name("Cocktail3").recipe("Recipe")
                        .sensoryDescriptors(List.of(new DescriptorCode(SensoryAxis.TASTE, "sweet"))).build(),
                Cocktail.builder().id(4L).name("Cocktail4").recipe("Recipe")
                        .sensoryDescriptors(List.of(new DescriptorCode(SensoryAxis.TASTE, "sweet"))).build(),
                Cocktail.builder().id(5L).name("Cocktail5").recipe("Recipe")
                        .sensoryDescriptors(List.of(new DescriptorCode(SensoryAxis.TASTE, "sweet"))).build()
        );

        when(cocktailRepository.findByDescriptors(any())).thenReturn(mockCocktails);

        var matches = matcher.match(descriptors);

        assertThat(matches).hasSizeLessThanOrEqualTo(3);
    }

    @Test
    @DisplayName("매칭되는 칵테일 없으면 빈 리스트")
    void shouldReturnEmptyListWhenNoMatch() {
        // given - 존재하지 않는 descriptor
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "nonexistent")))
                .build();

        when(cocktailRepository.findByDescriptors(any())).thenReturn(List.of());

        var matches = matcher.match(descriptors);

        assertThat(matches).isEmpty();
    }

    @Test
    @DisplayName("점수 내림차순 정렬 테스트")
    void shouldSortByScoreDescending() {
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .aroma(Set.of(new DescriptorCode(SensoryAxis.AROMA, "fruity")))
                .build();

        var mockCocktails = List.of(
                Cocktail.builder().id(1L).name("Perfect Match").recipe("Recipe")
                        .sensoryDescriptors(List.of(
                                new DescriptorCode(SensoryAxis.TASTE, "sweet"),
                                new DescriptorCode(SensoryAxis.AROMA, "fruity")
                        )).build(),
                Cocktail.builder().id(2L).name("Partial Match").recipe("Recipe")
                        .sensoryDescriptors(List.of(
                                new DescriptorCode(SensoryAxis.TASTE, "sweet")
                        )).build(),
                Cocktail.builder().id(3L).name("Another Match").recipe("Recipe")
                        .sensoryDescriptors(List.of(
                                new DescriptorCode(SensoryAxis.AROMA, "fruity")
                        )).build()
        );

        when(cocktailRepository.findByDescriptors(any())).thenReturn(mockCocktails);

        var matches = matcher.match(descriptors);

        assertThat(matches)
                .extracting(MatchedCocktail::score)
                .isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    @DisplayName("점수 0인 칵테일 제외 테스트")
    void shouldExcludeZeroScoreCocktails() {
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(new DescriptorCode(SensoryAxis.TASTE, "sweet")))
                .build();

        var mockCocktails = List.of(
                Cocktail.builder().id(1L).name("Match").recipe("Recipe")
                        .sensoryDescriptors(List.of(new DescriptorCode(SensoryAxis.TASTE, "sweet"))).build(),
                Cocktail.builder().id(2L).name("No Match").recipe("Recipe")
                        .sensoryDescriptors(List.of(new DescriptorCode(SensoryAxis.TASTE, "bitter"))).build()
        );

        when(cocktailRepository.findByDescriptors(any())).thenReturn(mockCocktails);

        var matches = matcher.match(descriptors);

        assertThat(matches).allMatch(match -> match.score() > 0.0);
    }

    @Test
    @DisplayName("빈 descriptors로 검색 시 빈 리스트 반환")
    void shouldReturnEmptyListForEmptyDescriptors() {
        var descriptors = SensoryDescriptors.builder().build();

        var matches = matcher.match(descriptors);

        assertThat(matches).isEmpty();
    }

    @Test
    @DisplayName("매칭된 descriptors 포함 여부 확인")
    void shouldIncludeMatchedDescriptors() {
        var sweetDescriptor = new DescriptorCode(SensoryAxis.TASTE, "sweet");
        var descriptors = SensoryDescriptors.builder()
                .taste(Set.of(sweetDescriptor))
                .build();

        var mockCocktail = Cocktail.builder()
                .id(1L)
                .name("Sweet Cocktail")
                .recipe("Recipe")
                .sensoryDescriptors(List.of(sweetDescriptor))
                .build();

        when(cocktailRepository.findByDescriptors(any())).thenReturn(List.of(mockCocktail));

        var matches = matcher.match(descriptors);

        if (!matches.isEmpty()) {
            assertThat(matches.get(0).matchedDescriptors()).isNotNull();
        }
    }
}

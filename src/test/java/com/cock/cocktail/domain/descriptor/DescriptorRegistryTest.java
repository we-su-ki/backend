package com.cock.cocktail.domain.descriptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DescriptorRegistryTest {

    private DescriptorRegistry registry;

    @BeforeEach
    void setUp() {
        var sweetDefinition = new DescriptorDefinition(
                "sweet",
                "달달한",
                List.of("달달한", "달콤한", "sweet"),
                List.of()
        );
        var sourDefinition = new DescriptorDefinition(
                "sour",
                "새콤한",
                List.of("새콤한", "신", "sour"),
                List.of()
        );
        var fruityDefinition = new DescriptorDefinition(
                "fruity",
                "과일향",
                List.of("과일향", "과일", "fruity"),
                List.of()
        );

        registry = new DescriptorRegistry(
                List.of(sweetDefinition, sourDefinition),
                List.of(fruityDefinition),
                List.of(),
                List.of(),
                List.of()
        );
    }

    @Test
    @DisplayName("TASTE 축에서 매칭되는 descriptor 찾기")
    void shouldFindMatchesInTasteAxis() {
        var matches = registry.findMatches("달콤한 칵테일", SensoryAxis.TASTE);

        assertThat(matches).hasSize(1);
        assertThat(matches.stream().map(DescriptorCode::value).toList())
                .containsExactly("sweet");
    }

    @Test
    @DisplayName("AROMA 축에서 매칭되는 descriptor 찾기")
    void shouldFindMatchesInAromaAxis() {
        var matches = registry.findMatches("과일향 나는 칵테일", SensoryAxis.AROMA);

        assertThat(matches).hasSize(1);
        assertThat(matches.stream().map(DescriptorCode::value).toList())
                .containsExactly("fruity");
    }

    @Test
    @DisplayName("매칭되는 것이 없으면 빈 Set 반환")
    void shouldReturnEmptySetWhenNoMatch() {
        var matches = registry.findMatches("알 수 없는 쿼리", SensoryAxis.TASTE);

        assertThat(matches).isEmpty();
    }

    @Test
    @DisplayName("여러 descriptor가 매칭되면 모두 반환")
    void shouldReturnAllMatchesWhenMultipleMatch() {
        var matches = registry.findMatches("달콤한 새콤한 칵테일", SensoryAxis.TASTE);

        assertThat(matches).hasSize(2);
        assertThat(matches.stream().map(DescriptorCode::value).toList())
                .containsExactlyInAnyOrder("sweet", "sour");
    }

    @Test
    @DisplayName("반환되는 DescriptorCode의 axis가 올바른지 확인")
    void shouldReturnDescriptorCodeWithCorrectAxis() {
        var matches = registry.findMatches("달콤한", SensoryAxis.TASTE);

        assertThat(matches).hasSize(1);
        var descriptorCode = matches.iterator().next();
        assertThat(descriptorCode.axis()).isEqualTo(SensoryAxis.TASTE);
        assertThat(descriptorCode.value()).isEqualTo("sweet");
    }

    @Test
    @DisplayName("빈 리스트로 초기화된 축에서 매칭 시도")
    void shouldReturnEmptyForEmptyAxisDefinitions() {
        var matches = registry.findMatches("아무 쿼리", SensoryAxis.MOUTHFEEL);

        assertThat(matches).isEmpty();
    }
}

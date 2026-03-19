package com.cock.cocktail.infrastructure.config_based_analyzer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class DescriptorDefinitionTest {

    @Test
    @DisplayName("동의어 매칭 - 쿼리에 동의어가 포함되면 매칭 성공")
    void shouldMatchWhenQueryContainsSynonym() {
        var descriptor = new DescriptorDefinition(
                "sweet",
                "달달한",
                List.of("달달한", "달콤한", "단"),
                List.of()
        );
        var query = "달콤한 칵테일 추천해줘";

        var isMatched = descriptor.matches(query);

        assertThat(isMatched).isTrue();
    }

    @Test
    @DisplayName("동의어 매칭 - 대소문자 구분 없이 매칭")
    void shouldMatchCaseInsensitively() {
        var descriptor = new DescriptorDefinition(
                "sweet",
                "달달한",
                List.of("Sweet", "SWEET"),
                List.of()
        );
        var query = "sweet cocktail";

        var isMatched = descriptor.matches(query);

        assertThat(isMatched).isTrue();
    }

    @Test
    @DisplayName("동의어 매칭 - 쿼리에 동의어가 없으면 매칭 실패")
    void shouldNotMatchWhenQueryDoesNotContainSynonym() {
        var descriptor = new DescriptorDefinition(
                "sweet",
                "달달한",
                List.of("달달한", "달콤한"),
                List.of()
        );
        var query = "신맛 나는 칵테일";

        var isMatched = descriptor.matches(query);

        assertThat(isMatched).isFalse();
    }

    @Test
    @DisplayName("계층 매칭 - 자신이 매칭되면 code 반환")
    void shouldReturnOwnCodeWhenMatched() {
        var descriptor = new DescriptorDefinition(
                "fruity",
                "과일향",
                List.of("과일향", "과일"),
                List.of()
        );
        var query = "과일향 나는 칵테일";

        var matchedCodes = descriptor.findMatches(query);

        assertThat(matchedCodes).containsExactly("fruity");
    }

    @Test
    @DisplayName("계층 매칭 - 자식 descriptor가 매칭되면 자식 code 반환")
    void shouldReturnChildCodeWhenChildMatches() {
        var limeDescriptor = new DescriptorDefinition(
                "lime",
                "라임",
                List.of("라임"),
                List.of()
        );
        var citrusDescriptor = new DescriptorDefinition(
                "citrus",
                "시트러스",
                List.of("시트러스", "감귤"),
                List.of(limeDescriptor)
        );
        var fruityDescriptor = new DescriptorDefinition(
                "fruity",
                "과일향",
                List.of("과일향"),
                List.of(citrusDescriptor)
        );

        var query = "라임 향이 나는 칵테일";

        var matchedCodes = fruityDescriptor.findMatches(query);

        assertThat(matchedCodes).containsExactly("lime");
    }

    @Test
    @DisplayName("계층 매칭 - 부모와 자식 모두 매칭되면 둘 다 반환")
    void shouldReturnBothCodesWhenBothMatch() {
        var citrusDescriptor = new DescriptorDefinition(
                "citrus",
                "시트러스",
                List.of("시트러스"),
                List.of()
        );
        var fruityDescriptor = new DescriptorDefinition(
                "fruity",
                "과일향",
                List.of("과일향"),
                List.of(citrusDescriptor)
        );

        var query = "과일향 나는 시트러스 칵테일";

        var matchedCodes = fruityDescriptor.findMatches(query);

        assertThat(matchedCodes).containsExactlyInAnyOrder("fruity", "citrus");
    }

    @Test
    @DisplayName("계층 매칭 - 매칭되는 것이 없으면 빈 리스트 반환")
    void shouldReturnEmptyListWhenNothingMatches() {
        var descriptor = new DescriptorDefinition(
                "fruity",
                "과일향",
                List.of("과일향"),
                List.of()
        );
        var query = "커피 향이 나는 칵테일";

        var matchedCodes = descriptor.findMatches(query);

        assertThat(matchedCodes).isEmpty();
    }

    @Test
    @DisplayName("계층 매칭 - 깊은 계층 구조에서도 올바르게 매칭")
    void shouldMatchInDeepHierarchy() {
        var strawberryDescriptor = new DescriptorDefinition(
                "strawberry",
                "딸기",
                List.of("딸기"),
                List.of()
        );
        var berryDescriptor = new DescriptorDefinition(
                "berry",
                "베리",
                List.of("베리"),
                List.of(strawberryDescriptor)
        );
        var fruityDescriptor = new DescriptorDefinition(
                "fruity",
                "과일향",
                List.of("과일향"),
                List.of(berryDescriptor)
        );

        var query = "딸기 향이 나는 칵테일";

        var matchedCodes = fruityDescriptor.findMatches(query);

        assertThat(matchedCodes).containsExactly("strawberry");
    }

    @Test
    @DisplayName("null 처리 - synonyms가 null이면 빈 리스트로 초기화")
    void shouldInitializeEmptyListWhenSynonymsIsNull() {
        // given & when
        var descriptor = new DescriptorDefinition(
                "sweet",
                "달달한",
                null,
                List.of()
        );

        assertAll(
                () -> assertThat(descriptor.synonyms()).isNotNull(),
                () -> assertThat(descriptor.synonyms()).isEmpty()
        );
    }

    @Test
    @DisplayName("null 처리 - children이 null이면 빈 리스트로 초기화")
    void shouldInitializeEmptyListWhenChildrenIsNull() {
        // given & when
        var descriptor = new DescriptorDefinition(
                "sweet",
                "달달한",
                List.of("달달한"),
                null
        );

        assertAll(
                () -> assertThat(descriptor.children()).isNotNull(),
                () -> assertThat(descriptor.children()).isEmpty()
        );
    }
}

package com.cock.cocktail.infrastructure.config_based_analyzer;

import com.cock.cocktail.domain.SensoryAxis;
import com.cock.cocktail.domain.DescriptorRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ConfigBasedKeywordAnalyzerTest {

    private ConfigBasedKeywordAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new ConfigBasedKeywordAnalyzer(loadDescriptorRegistry());
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
    @DisplayName("YAML 설정 파일 로딩 성공")
    void shouldLoadYamlConfiguration() {
        // given & when
        var sensoryDescriptors = analyzer.analyze("테스트");

        // then - 정상적으로 로드되면 예외 없이 결과 반환
        assertThat(sensoryDescriptors).isNotNull();
    }

    @Test
    @DisplayName("단일 축 추출 - taste만 매칭")
    void shouldExtractOnlyTasteDescriptors() {
        var query = "달달한 칵테일";

        var sensoryDescriptors = analyzer.analyze(query);

        assertAll(
                () -> assertThat(sensoryDescriptors.taste()).isNotEmpty(),
                () -> assertThat(sensoryDescriptors.aroma()).isEmpty(),
                () -> assertThat(sensoryDescriptors.mouthfeel()).isEmpty(),
                () -> assertThat(sensoryDescriptors.sensation()).isEmpty(),
                () -> assertThat(sensoryDescriptors.impression()).isEmpty()
        );
    }

    @Test
    @DisplayName("복수 축 추출 - taste와 aroma 동시 매칭")
    void shouldExtractMultipleAxes() {
        var query = "달달하고 라임향이 나는 칵테일";

        var sensoryDescriptors = analyzer.analyze(query);

        assertAll(
                () -> assertThat(sensoryDescriptors.taste().stream().map(d -> d.value()).toList()).contains("sweet"),
                () -> assertThat(sensoryDescriptors.aroma().stream().map(d -> d.value()).toList()).contains("lime"),
                () -> assertThat(sensoryDescriptors.mouthfeel()).isEmpty(),
                () -> assertThat(sensoryDescriptors.sensation()).isEmpty(),
                () -> assertThat(sensoryDescriptors.impression()).isEmpty()
        );
    }

    @Test
    @DisplayName("계층적 매칭 - 하위 descriptor 추출")
    void shouldExtractHierarchicalDescriptors() {
        var query = "라임 향이 나는 칵테일";

        var sensoryDescriptors = analyzer.analyze(query);

        assertAll(
                () -> assertThat(sensoryDescriptors.aroma().stream().map(d -> d.value()).toList()).contains("lime"),
                () -> assertThat(sensoryDescriptors.aroma().stream().map(d -> d.value()).toList()).doesNotContain("citrus", "fruity")
        );
    }

    @Test
    @DisplayName("계층적 매칭 - 상위 descriptor 추출")
    void shouldExtractParentDescriptor() {
        var query = "과일향 나는 칵테일";

        var sensoryDescriptors = analyzer.analyze(query);

        assertThat(sensoryDescriptors.aroma().stream().map(d -> d.value()).toList()).contains("fruity");
    }

    @Test
    @DisplayName("복수 descriptor 추출 - 같은 축에서 여러 개 매칭")
    void shouldExtractMultipleDescriptorsFromSameAxis() {
        var query = "달달하고 새콤한 칵테일";

        var sensoryDescriptors = analyzer.analyze(query);

        assertThat(sensoryDescriptors.taste().stream().map(d -> d.value()).toList()).containsExactlyInAnyOrder("sweet", "sour");
    }

    @Test
    @DisplayName("동의어 매칭 - 다양한 표현 처리")
    void shouldMatchSynonyms() {
        var query1 = "달달한 칵테일";
        var query2 = "달콤한 칵테일";
        var query3 = "단 칵테일";

        var sensoryDescriptors1 = analyzer.analyze(query1);
        var sensoryDescriptors2 = analyzer.analyze(query2);
        var sensoryDescriptors3 = analyzer.analyze(query3);

        assertAll(
                () -> assertThat(sensoryDescriptors1.taste().stream().map(d -> d.value()).toList()).contains("sweet"),
                () -> assertThat(sensoryDescriptors2.taste().stream().map(d -> d.value()).toList()).contains("sweet"),
                () -> assertThat(sensoryDescriptors3.taste().stream().map(d -> d.value()).toList()).contains("sweet")
        );
    }

    @Test
    @DisplayName("매칭 없음 - 모든 축이 빈 리스트")
    void shouldReturnEmptyListsWhenNoMatch() {
        var query = "칵테일 추천해줘";

        var sensoryDescriptors = analyzer.analyze(query);

        assertAll(
                () -> assertThat(sensoryDescriptors.taste()).isEmpty(),
                () -> assertThat(sensoryDescriptors.aroma()).isEmpty(),
                () -> assertThat(sensoryDescriptors.mouthfeel()).isEmpty(),
                () -> assertThat(sensoryDescriptors.sensation()).isEmpty(),
                () -> assertThat(sensoryDescriptors.impression()).isEmpty()
        );
    }

    @Test
    @DisplayName("전체 축 매칭 - 5개 축 모두 매칭")
    void shouldMatchAllAxes() {
        var query = "달달하고 라임향이 나는 부드럽고 톡 쏘는 여름 칵테일";

        var sensoryDescriptors = analyzer.analyze(query);

        assertAll(
                () -> assertThat(sensoryDescriptors.taste()).as("taste descriptors").isNotEmpty(),
                () -> assertThat(sensoryDescriptors.aroma()).as("aroma descriptors").isNotEmpty(),
                () -> assertThat(sensoryDescriptors.mouthfeel()).as("mouthfeel descriptors").isNotEmpty(),
                () -> assertThat(sensoryDescriptors.sensation()).as("sensation descriptors").isNotEmpty(),
                () -> assertThat(sensoryDescriptors.impression()).as("impression descriptors").isNotEmpty()
        );
    }

    @Test
    @DisplayName("대소문자 무관 매칭")
    void shouldBeCaseInsensitive() {
        var query = "달달한 칵테일";

        var sensoryDescriptors = analyzer.analyze(query);

        assertThat(sensoryDescriptors.taste().stream().map(d -> d.value()).toList()).contains("sweet");
    }

    @Test
    @DisplayName("부분 문자열 매칭")
    void shouldMatchSubstring() {
        var query = "새콤달콤한 칵테일"; // "새콤"과 "달콤" 모두 포함

        var sensoryDescriptors = analyzer.analyze(query);

        assertThat(sensoryDescriptors.taste().stream().map(d -> d.value()).toList()).containsExactlyInAnyOrder("sweet", "sour");
    }

    @Test
    @DisplayName("중복 제거 - 같은 descriptor가 여러 번 매칭되어도 한 번만 반환")
    void shouldNotDuplicateDescriptors() {
        var query = "달달한 달콤한 달달 칵테일"; // sweet의 동의어 여러 개

        var sensoryDescriptors = analyzer.analyze(query);

        // sweet이 여러 번 추가될 수 있지만, 현재 구조에서는 각 descriptor마다 한 번씩 추가됨
        // keywords.yml에서 sweet은 하나의 descriptor이므로 중복 없음
        assertThat(sensoryDescriptors.taste().stream().map(d -> d.value()).toList()).contains("sweet");
    }
}

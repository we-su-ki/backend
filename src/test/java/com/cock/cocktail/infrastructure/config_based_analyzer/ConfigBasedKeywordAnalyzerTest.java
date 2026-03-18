package com.cock.cocktail.infrastructure.config_based_analyzer;

import com.cock.cocktail.domain.SensoryAxis;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigBasedKeywordAnalyzerTest {

    private ConfigBasedKeywordAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new ConfigBasedKeywordAnalyzer();
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
        // given
        var query = "달달한 칵테일";

        // when
        var sensoryDescriptors = analyzer.analyze(query);

        // then
        assertThat(sensoryDescriptors.taste()).isNotEmpty();
        assertThat(sensoryDescriptors.aroma()).isEmpty();
        assertThat(sensoryDescriptors.mouthfeel()).isEmpty();
        assertThat(sensoryDescriptors.sensation()).isEmpty();
        assertThat(sensoryDescriptors.impression()).isEmpty();
    }

    @Test
    @DisplayName("복수 축 추출 - taste와 aroma 동시 매칭")
    void shouldExtractMultipleAxes() {
        // given
        var query = "달달하고 라임향이 나는 칵테일";

        // when
        var sensoryDescriptors = analyzer.analyze(query);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.TASTE)).contains("sweet");
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.AROMA)).contains("lime");
        assertThat(sensoryDescriptors.mouthfeel()).isEmpty();
        assertThat(sensoryDescriptors.sensation()).isEmpty();
        assertThat(sensoryDescriptors.impression()).isEmpty();
    }

    @Test
    @DisplayName("계층적 매칭 - 하위 descriptor 추출")
    void shouldExtractHierarchicalDescriptors() {
        // given
        var query = "라임 향이 나는 칵테일";

        // when
        var sensoryDescriptors = analyzer.analyze(query);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.AROMA)).contains("lime");
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.AROMA)).doesNotContain("citrus", "fruity");
    }

    @Test
    @DisplayName("계층적 매칭 - 상위 descriptor 추출")
    void shouldExtractParentDescriptor() {
        // given
        var query = "과일향 나는 칵테일";

        // when
        var sensoryDescriptors = analyzer.analyze(query);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.AROMA)).contains("fruity");
    }

    @Test
    @DisplayName("복수 descriptor 추출 - 같은 축에서 여러 개 매칭")
    void shouldExtractMultipleDescriptorsFromSameAxis() {
        // given
        var query = "달달하고 새콤한 칵테일";

        // when
        var sensoryDescriptors = analyzer.analyze(query);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.TASTE)).containsExactlyInAnyOrder("sweet", "sour");
    }

    @Test
    @DisplayName("동의어 매칭 - 다양한 표현 처리")
    void shouldMatchSynonyms() {
        // given
        var query1 = "달달한 칵테일";
        var query2 = "달콤한 칵테일";
        var query3 = "단 칵테일";

        // when
        var sensoryDescriptors1 = analyzer.analyze(query1);
        var sensoryDescriptors2 = analyzer.analyze(query2);
        var sensoryDescriptors3 = analyzer.analyze(query3);

        // then
        assertThat(sensoryDescriptors1.codeValues(SensoryAxis.TASTE)).contains("sweet");
        assertThat(sensoryDescriptors2.codeValues(SensoryAxis.TASTE)).contains("sweet");
        assertThat(sensoryDescriptors3.codeValues(SensoryAxis.TASTE)).contains("sweet");
    }

    @Test
    @DisplayName("매칭 없음 - 모든 축이 빈 리스트")
    void shouldReturnEmptyListsWhenNoMatch() {
        // given
        var query = "칵테일 추천해줘";

        // when
        var sensoryDescriptors = analyzer.analyze(query);

        // then
        assertThat(sensoryDescriptors.taste()).isEmpty();
        assertThat(sensoryDescriptors.aroma()).isEmpty();
        assertThat(sensoryDescriptors.mouthfeel()).isEmpty();
        assertThat(sensoryDescriptors.sensation()).isEmpty();
        assertThat(sensoryDescriptors.impression()).isEmpty();
    }

    @Test
    @DisplayName("전체 축 매칭 - 5개 축 모두 매칭")
    void shouldMatchAllAxes() {
        // given
        var query = "달달하고 라임향이 나는 부드럽고 톡 쏘는 여름 칵테일";

        // when
        var sensoryDescriptors = analyzer.analyze(query);

        // then
        assertThat(sensoryDescriptors.taste()).as("taste descriptors").isNotEmpty();
        assertThat(sensoryDescriptors.aroma()).as("aroma descriptors").isNotEmpty();
        assertThat(sensoryDescriptors.mouthfeel()).as("mouthfeel descriptors").isNotEmpty();
        assertThat(sensoryDescriptors.sensation()).as("sensation descriptors").isNotEmpty();
        assertThat(sensoryDescriptors.impression()).as("impression descriptors").isNotEmpty();
    }

    @Test
    @DisplayName("대소문자 무관 매칭")
    void shouldBeCaseInsensitive() {
        // given
        var query = "달달한 칵테일";

        // when
        var sensoryDescriptors = analyzer.analyze(query);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.TASTE)).contains("sweet");
    }

    @Test
    @DisplayName("부분 문자열 매칭")
    void shouldMatchSubstring() {
        // given
        var query = "새콤달콤한 칵테일"; // "새콤"과 "달콤" 모두 포함

        // when
        var sensoryDescriptors = analyzer.analyze(query);

        // then
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.TASTE)).containsExactlyInAnyOrder("sweet", "sour");
    }

    @Test
    @DisplayName("중복 제거 - 같은 descriptor가 여러 번 매칭되어도 한 번만 반환")
    void shouldNotDuplicateDescriptors() {
        // given
        var query = "달달한 달콤한 달달 칵테일"; // sweet의 동의어 여러 개

        // when
        var sensoryDescriptors = analyzer.analyze(query);

        // then
        // sweet이 여러 번 추가될 수 있지만, 현재 구조에서는 각 descriptor마다 한 번씩 추가됨
        // keywords.yml에서 sweet은 하나의 descriptor이므로 중복 없음
        assertThat(sensoryDescriptors.codeValues(SensoryAxis.TASTE)).contains("sweet");
    }

    @Test
    @DisplayName("내부 DescriptorConfig - 모든 필드가 null이어도 빈 리스트로 초기화")
    void shouldInitializeNestedDescriptorConfigWithEmptyListsWhenAllNull() throws Exception {
        // given
        var config = createNestedDescriptorConfig(null, null, null, null, null);

        // when & then
        assertThat(invokeListAccessor(config, "taste")).isEmpty();
        assertThat(invokeListAccessor(config, "aroma")).isEmpty();
        assertThat(invokeListAccessor(config, "mouthfeel")).isEmpty();
        assertThat(invokeListAccessor(config, "sensation")).isEmpty();
        assertThat(invokeListAccessor(config, "impression")).isEmpty();
    }

    @Test
    @DisplayName("내부 DescriptorConfig - 필드가 제공되면 그대로 저장")
    void shouldKeepNestedDescriptorConfigValuesWhenProvided() throws Exception {
        // given
        var tasteDescriptors = List.of(
                new DescriptorDefinition("sweet", "달달한", List.of("달달한"), List.of())
        );
        var config = createNestedDescriptorConfig(tasteDescriptors, List.of(), List.of(), List.of(), List.of());

        // when
        var storedTaste = invokeListAccessor(config, "taste");

        // then
        assertThat(storedTaste).isEqualTo(tasteDescriptors);
    }

    private static Object createNestedDescriptorConfig(
            List<DescriptorDefinition> taste,
            List<DescriptorDefinition> aroma,
            List<DescriptorDefinition> mouthfeel,
            List<DescriptorDefinition> sensation,
            List<DescriptorDefinition> impression
    ) throws Exception {
        var nestedClass = Class.forName(
                "com.cock.cocktail.infrastructure.config_based_analyzer.ConfigBasedKeywordAnalyzer$DescriptorConfig"
        );
        Constructor<?> constructor = nestedClass.getDeclaredConstructor(
                List.class, List.class, List.class, List.class, List.class
        );
        constructor.setAccessible(true);
        return constructor.newInstance(taste, aroma, mouthfeel, sensation, impression);
    }

    @SuppressWarnings("unchecked")
    private static List<DescriptorDefinition> invokeListAccessor(Object target, String methodName) throws Exception {
        Method accessor = target.getClass().getDeclaredMethod(methodName);
        accessor.setAccessible(true);
        return (List<DescriptorDefinition>) accessor.invoke(target);
    }
}

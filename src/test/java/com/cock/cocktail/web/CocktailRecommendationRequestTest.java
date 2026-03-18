package com.cock.cocktail.web;

import com.cock.cocktail.web.dto.CocktailRecommendationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CocktailRecommendationRequestTest {

    private ObjectMapper objectMapper;
    private Validator validator;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("JSON을 DTO로 역직렬화")
    void shouldDeserializeFromJson() throws Exception {
        // given
        var jsonString = "{\"query\":\"달달한 칵테일 추천해줘\"}";

        // when
        var deserializedRequest = objectMapper.readValue(jsonString, CocktailRecommendationRequest.class);

        // then
        assertAll(
                () -> assertThat(deserializedRequest).isNotNull(),
                () -> assertThat(deserializedRequest.query()).isEqualTo("달달한 칵테일 추천해줘")
        );
    }

    @Test
    @DisplayName("DTO를 JSON으로 직렬화")
    void shouldSerializeToJson() throws Exception {
        // given
        var requestDto = new CocktailRecommendationRequest("달달한 칵테일 추천해줘");

        // when
        var serializedJson = objectMapper.writeValueAsString(requestDto);

        // then
        assertAll(
                () -> assertThat(serializedJson).contains("\"query\""),
                () -> assertThat(serializedJson).contains("달달한 칵테일 추천해줘")
        );
    }

    @Test
    @DisplayName("query 필드가 null이면 검증 실패")
    void shouldFailValidationWhenQueryIsNull() {
        // given
        var invalidRequest = new CocktailRecommendationRequest(null);

        // when
        var validationViolations = validator.validate(invalidRequest);

        // then
        assertThat(validationViolations).isNotEmpty();
    }

    @Test
    @DisplayName("query 필드가 빈 문자열이면 검증 실패")
    void shouldFailValidationWhenQueryIsBlank() {
        // given
        var invalidRequest = new CocktailRecommendationRequest("");

        // when
        var validationViolations = validator.validate(invalidRequest);

        // then
        assertThat(validationViolations).isNotEmpty();
    }

    @Test
    @DisplayName("query 필드가 공백만 있으면 검증 실패")
    void shouldFailValidationWhenQueryIsWhitespace() {
        // given
        var invalidRequest = new CocktailRecommendationRequest("   ");

        // when
        var validationViolations = validator.validate(invalidRequest);

        // then
        assertThat(validationViolations).isNotEmpty();
    }

    @Test
    @DisplayName("정상적인 query 값이면 검증 통과")
    void shouldPassValidationWhenQueryIsValid() {
        // given
        var validRequest = new CocktailRecommendationRequest("달달한 칵테일 추천해줘");

        // when
        var validationViolations = validator.validate(validRequest);

        // then
        assertThat(validationViolations).isEmpty();
    }
}

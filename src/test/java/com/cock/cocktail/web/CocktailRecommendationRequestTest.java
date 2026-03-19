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
        var jsonString = "{\"query\":\"달달한 칵테일 추천해줘\"}";

        var deserializedRequest = objectMapper.readValue(jsonString, CocktailRecommendationRequest.class);

        assertAll(
                () -> assertThat(deserializedRequest).isNotNull(),
                () -> assertThat(deserializedRequest.query()).isEqualTo("달달한 칵테일 추천해줘")
        );
    }

    @Test
    @DisplayName("DTO를 JSON으로 직렬화")
    void shouldSerializeToJson() throws Exception {
        var requestDto = new CocktailRecommendationRequest("달달한 칵테일 추천해줘");

        var serializedJson = objectMapper.writeValueAsString(requestDto);

        assertAll(
                () -> assertThat(serializedJson).contains("\"query\""),
                () -> assertThat(serializedJson).contains("달달한 칵테일 추천해줘")
        );
    }

    @Test
    @DisplayName("query 필드가 null이면 검증 실패")
    void shouldFailValidationWhenQueryIsNull() {
        var invalidRequest = new CocktailRecommendationRequest(null);

        var validationViolations = validator.validate(invalidRequest);

        assertThat(validationViolations).isNotEmpty();
    }

    @Test
    @DisplayName("query 필드가 빈 문자열이면 검증 실패")
    void shouldFailValidationWhenQueryIsBlank() {
        var invalidRequest = new CocktailRecommendationRequest("");

        var validationViolations = validator.validate(invalidRequest);

        assertThat(validationViolations).isNotEmpty();
    }

    @Test
    @DisplayName("query 필드가 공백만 있으면 검증 실패")
    void shouldFailValidationWhenQueryIsWhitespace() {
        var invalidRequest = new CocktailRecommendationRequest("   ");

        var validationViolations = validator.validate(invalidRequest);

        assertThat(validationViolations).isNotEmpty();
    }

    @Test
    @DisplayName("정상적인 query 값이면 검증 통과")
    void shouldPassValidationWhenQueryIsValid() {
        var validRequest = new CocktailRecommendationRequest("달달한 칵테일 추천해줘");

        var validationViolations = validator.validate(validRequest);

        assertThat(validationViolations).isEmpty();
    }
}

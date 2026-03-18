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

class CocktailRecommendationRequestTest {

    private ObjectMapper objectMapper;
    private Validator validator;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("JSON을 DTO로 역직렬화")
    void shouldDeserializeFromJson() throws Exception {
        // given
        String json = "{\"query\":\"달달한 칵테일 추천해줘\"}";

        // when
        CocktailRecommendationRequest request = objectMapper.readValue(json, CocktailRecommendationRequest.class);

        // then
        assertThat(request).isNotNull();
        assertThat(request.query()).isEqualTo("달달한 칵테일 추천해줘");
    }

    @Test
    @DisplayName("DTO를 JSON으로 직렬화")
    void shouldSerializeToJson() throws Exception {
        // given
        CocktailRecommendationRequest request = new CocktailRecommendationRequest("달달한 칵테일 추천해줘");

        // when
        String json = objectMapper.writeValueAsString(request);

        // then
        assertThat(json).contains("\"query\"");
        assertThat(json).contains("달달한 칵테일 추천해줘");
    }

    @Test
    @DisplayName("query 필드가 null이면 검증 실패")
    void shouldFailValidationWhenQueryIsNull() {
        // given
        CocktailRecommendationRequest request = new CocktailRecommendationRequest(null);

        // when
        Set<ConstraintViolation<CocktailRecommendationRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("query 필드가 빈 문자열이면 검증 실패")
    void shouldFailValidationWhenQueryIsBlank() {
        // given
        CocktailRecommendationRequest request = new CocktailRecommendationRequest("");

        // when
        Set<ConstraintViolation<CocktailRecommendationRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("query 필드가 공백만 있으면 검증 실패")
    void shouldFailValidationWhenQueryIsWhitespace() {
        // given
        CocktailRecommendationRequest request = new CocktailRecommendationRequest("   ");

        // when
        Set<ConstraintViolation<CocktailRecommendationRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("정상적인 query 값이면 검증 통과")
    void shouldPassValidationWhenQueryIsValid() {
        // given
        CocktailRecommendationRequest request = new CocktailRecommendationRequest("달달한 칵테일 추천해줘");

        // when
        Set<ConstraintViolation<CocktailRecommendationRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }
}

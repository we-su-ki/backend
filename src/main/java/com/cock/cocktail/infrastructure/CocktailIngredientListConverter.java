package com.cock.cocktail.infrastructure;

import com.cock.cocktail.domain.CocktailIngredient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter
public class CocktailIngredientListConverter implements AttributeConverter<List<CocktailIngredient>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final TypeReference<List<CocktailIngredient>> TYPE_REF = new TypeReference<>() {};

    @Override
    public String convertToDatabaseColumn(List<CocktailIngredient> attribute) {
        if (attribute == null) return null;
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("List<CocktailIngredient>를 JSON으로 변환 실패", e);
        }
    }

    @Override
    public List<CocktailIngredient> convertToEntityAttribute(String dbData) {
        if (dbData == null) return new ArrayList<>();
        try {
            return objectMapper.readValue(dbData, TYPE_REF);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON을 List<CocktailIngredient>로 변환 실패", e);
        }
    }
}

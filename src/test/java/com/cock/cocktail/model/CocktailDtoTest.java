package com.cock.cocktail.model;

import com.cock.cocktail.model.dto.CocktailDto;
import com.cock.cocktail.model.dto.Ingredient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CocktailDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("JSON을 CocktailDto로 역직렬화")
    void shouldDeserializeFromJson() throws Exception {
        // given
        String json = """
                {
                    "id": "mojito",
                    "name": "모히또",
                    "ingredients": [
                        {"name": "럼", "amount": "50ml"},
                        {"name": "라임", "amount": "20ml"}
                    ],
                    "recipe": ["얼음을 넣는다", "재료를 섞는다"],
                    "reason": "상큼한 맛이 어울립니다",
                    "tags": ["상큼한", "시원한"]
                }
                """;

        // when
        CocktailDto cocktail = objectMapper.readValue(json, CocktailDto.class);

        // then
        assertThat(cocktail).isNotNull();
        assertThat(cocktail.getId()).isEqualTo("mojito");
        assertThat(cocktail.getName()).isEqualTo("모히또");
        assertThat(cocktail.getIngredients()).hasSize(2);
        assertThat(cocktail.getRecipe()).hasSize(2);
        assertThat(cocktail.getReason()).isEqualTo("상큼한 맛이 어울립니다");
        assertThat(cocktail.getTags()).hasSize(2);
    }

    @Test
    @DisplayName("CocktailDto를 JSON으로 직렬화")
    void shouldSerializeToJson() throws Exception {
        // given
        CocktailDto cocktail = new CocktailDto();
        cocktail.setId("mojito");
        cocktail.setName("모히또");
        cocktail.setIngredients(List.of(
                new Ingredient("럼", "50ml"),
                new Ingredient("라임", "20ml")
        ));
        cocktail.setRecipe(List.of("얼음을 넣는다", "재료를 섞는다"));
        cocktail.setReason("상큼한 맛이 어울립니다");
        cocktail.setTags(List.of("상큼한", "시원한"));

        // when
        String json = objectMapper.writeValueAsString(cocktail);

        // then
        assertThat(json).contains("\"id\":\"mojito\"");
        assertThat(json).contains("\"name\":\"모히또\"");
        assertThat(json).contains("\"ingredients\"");
        assertThat(json).contains("\"recipe\"");
        assertThat(json).contains("\"reason\"");
        assertThat(json).contains("\"tags\"");
    }
}

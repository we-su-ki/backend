package com.cock.cocktail.web;

import com.cock.cocktail.domain.Ingredient;
import com.cock.cocktail.web.dto.CocktailDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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
                    "id": 1,
                    "name": "모히또",
                    "ingredients": [
                        {"name": "럼", "amount": "50ml"},
                        {"name": "라임", "amount": "20ml"}
                    ],
                    "recipe": "얼음을 넣는다\\n재료를 섞는다",
                    "reason": "상큼한 맛이 어울립니다",
                    "tags": {
                        "taste": ["상큼한"],
                        "mood": ["시원한"]
                    }
                }
                """;

        // when
        CocktailDto cocktail = objectMapper.readValue(json, CocktailDto.class);

        // then
        assertThat(cocktail).isNotNull();
        assertThat(cocktail.id()).isEqualTo(1L);
        assertThat(cocktail.name()).isEqualTo("모히또");
        assertThat(cocktail.ingredients()).hasSize(2);
        assertThat(cocktail.recipe()).contains("얼음을 넣는다");
        assertThat(cocktail.reason()).isEqualTo("상큼한 맛이 어울립니다");
        assertThat(cocktail.tags()).hasSize(2);
        assertThat(cocktail.tags().get("taste")).contains("상큼한");
    }

    @Test
    @DisplayName("CocktailDto를 JSON으로 직렬화")
    void shouldSerializeToJson() throws Exception {
        // given
        CocktailDto cocktail = new CocktailDto(
                1L,
                "모히또",
                List.of(
                        new Ingredient("럼", "50ml"),
                        new Ingredient("라임", "20ml")
                ),
                "얼음을 넣는다\n재료를 섞는다",
                "상큼한 맛이 어울립니다",
                Map.of(
                        "taste", List.of("상큼한"),
                        "mood", List.of("시원한")
                )
        );

        // when
        String json = objectMapper.writeValueAsString(cocktail);

        // then
        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"name\":\"모히또\"");
        assertThat(json).contains("\"ingredients\"");
        assertThat(json).contains("\"recipe\"");
        assertThat(json).contains("\"reason\"");
        assertThat(json).contains("\"tags\"");
    }
}

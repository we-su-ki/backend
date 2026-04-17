package com.cock.cocktail.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CocktailRepositoryTest {

    @Autowired
    private CocktailRepository cocktailRepository;

    @Test
    @DisplayName("전체 칵테일 조회 - 재료 함께 로드")
    void shouldFindAllCocktailsWithIngredients() {
        var cocktails = cocktailRepository.findAll();

        assertThat(cocktails).isNotEmpty();
        assertThat(cocktails).allMatch(c -> !c.getCocktailIngredients().isEmpty());
    }
}

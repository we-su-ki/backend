package com.cock.cocktail.domain;

import com.cock.cocktail.repository.CocktailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CocktailRepositoryTest {

    @Autowired
    private CocktailRepository cocktailRepository;

    @Test
    @DisplayName("데이터베이스에서 칵테일 데이터 로드")
    void shouldLoadCocktailsFromDatabase() {
        // when
        List<Cocktail> cocktails = cocktailRepository.findAll();

        // then
        assertThat(cocktails).isNotEmpty();
        assertThat(cocktails.size()).isGreaterThanOrEqualTo(10);
    }

    @Test
    @DisplayName("전체 칵테일 조회")
    void shouldReturnAllCocktails() {
        // when
        List<Cocktail> cocktails = cocktailRepository.findAll();

        // then
        assertThat(cocktails).isNotNull();
        assertThat(cocktails).allMatch(c -> c.getId() != null);
        assertThat(cocktails).allMatch(c -> c.getName() != null);
        assertThat(cocktails).allMatch(c -> c.getIngredients() != null);
    }

    @Test
    @DisplayName("ID로 칵테일 조회 - 존재하는 경우")
    void shouldFindCocktailById() {
        // given
        Cocktail savedCocktail = cocktailRepository.findAll().get(0);
        Long cocktailId = savedCocktail.getId();

        // when
        Optional<Cocktail> cocktail = cocktailRepository.findById(cocktailId);

        // then
        assertThat(cocktail).isPresent();
        assertThat(cocktail.get().getId()).isEqualTo(cocktailId);
        assertThat(cocktail.get().getName()).isNotBlank();
    }

    @Test
    @DisplayName("ID로 칵테일 조회 - 존재하지 않는 경우")
    void shouldReturnEmptyWhenCocktailNotFound() {
        // given
        Long nonExistentId = 999999L;

        // when
        Optional<Cocktail> cocktail = cocktailRepository.findById(nonExistentId);

        // then
        assertThat(cocktail).isEmpty();
    }

    @Test
    @DisplayName("태그로 칵테일 검색 - 맛 태그")
    void shouldFindCocktailsByTasteTag() {
        // given
        String tasteTag = "달달한";

        // when
        List<Cocktail> cocktails = cocktailRepository.findByTag("taste", tasteTag);

        // then
        assertThat(cocktails).isNotEmpty();
        assertThat(cocktails).allMatch(c -> c.getTags().get("taste").contains(tasteTag));
    }

    @Test
    @DisplayName("태그로 칵테일 검색 - 향 태그")
    void shouldFindCocktailsByFlavorTag() {
        // given
        String flavorTag = "과일향";

        // when
        List<Cocktail> cocktails = cocktailRepository.findByTag("flavor", flavorTag);

        // then
        assertThat(cocktails).isNotEmpty();
        assertThat(cocktails).allMatch(c -> c.getTags().get("flavor").contains(flavorTag));
    }

    @Test
    @DisplayName("태그로 칵테일 검색 - 매칭되는 칵테일 없음")
    void shouldReturnEmptyListWhenNoMatchingTag() {
        // given
        String nonExistentTag = "존재하지않는태그";

        // when
        List<Cocktail> cocktails = cocktailRepository.findByTag("taste", nonExistentTag);

        // then
        assertThat(cocktails).isEmpty();
    }

    @Test
    @DisplayName("칵테일 데이터에 필수 필드가 모두 존재")
    void shouldHaveAllRequiredFields() {
        // when
        List<Cocktail> cocktails = cocktailRepository.findAll();

        // then
        assertThat(cocktails).isNotEmpty();
        for (Cocktail cocktail : cocktails) {
            assertThat(cocktail.getId()).isNotNull();
            assertThat(cocktail.getName()).isNotBlank();
            assertThat(cocktail.getIngredients()).isNotEmpty();
            assertThat(cocktail.getRecipe()).isNotBlank();
            assertThat(cocktail.getTags()).isNotEmpty();
        }
    }
}

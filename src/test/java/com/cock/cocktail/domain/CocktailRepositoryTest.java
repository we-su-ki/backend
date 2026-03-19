package com.cock.cocktail.domain;

import com.cock.cocktail.repository.CocktailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class CocktailRepositoryTest {

    @Autowired
    private CocktailRepository cocktailRepository;

    @Test
    @DisplayName("데이터베이스에서 칵테일 데이터 로드")
    void shouldLoadCocktailsFromDatabase() {
        // when
        var allCocktails = cocktailRepository.findAll();

        // then
        assertAll(
                () -> assertThat(allCocktails).isNotEmpty(),
                () -> assertThat(allCocktails.size()).isGreaterThanOrEqualTo(10)
        );
    }

    @Test
    @DisplayName("전체 칵테일 조회")
    void shouldReturnAllCocktails() {
        // when
        var allCocktails = cocktailRepository.findAll();

        // then
        assertAll(
                () -> assertThat(allCocktails).isNotNull(),
                () -> assertThat(allCocktails).allMatch(c -> c.getId() != null),
                () -> assertThat(allCocktails).allMatch(c -> c.getName() != null),
                () -> assertThat(allCocktails).allMatch(c -> c.getIngredients() != null)
        );
    }

    @Test
    @DisplayName("ID로 칵테일 조회 - 존재하는 경우")
    void shouldFindCocktailById() {
        // given
        var firstCocktail = cocktailRepository.findAll().get(0);
        var existingCocktailId = firstCocktail.getId();

        // when
        var foundCocktail = cocktailRepository.findById(existingCocktailId);

        // then
        assertAll(
                () -> assertThat(foundCocktail).isPresent(),
                () -> assertThat(foundCocktail.get().getId()).isEqualTo(existingCocktailId),
                () -> assertThat(foundCocktail.get().getName()).isNotBlank()
        );
    }

    @Test
    @DisplayName("ID로 칵테일 조회 - 존재하지 않는 경우")
    void shouldReturnEmptyWhenCocktailNotFound() {
        // given
        var nonExistentCocktailId = 999999L;

        // when
        var foundCocktail = cocktailRepository.findById(nonExistentCocktailId);

        // then
        assertThat(foundCocktail).isEmpty();
    }

    @Test
    @DisplayName("태그로 칵테일 검색 - 맛 태그")
    void shouldFindCocktailsByTasteTag() {
        // given
        var searchTasteTag = "sweet";

        // when
        var matchedCocktails = cocktailRepository.findByTag("taste", searchTasteTag);

        // then
        assertAll(
                () -> assertThat(matchedCocktails).isNotEmpty(),
                () -> assertThat(matchedCocktails).allMatch(
                        c -> c.getSensoryDescriptors().codeValues(SensoryAxis.TASTE).contains(searchTasteTag)
                )
        );
    }

    @Test
    @DisplayName("태그로 칵테일 검색 - 향 태그")
    void shouldFindCocktailsByFlavorTag() {
        // given
        var searchFlavorTag = "fruity";

        // when
        var matchedCocktails = cocktailRepository.findByTag("flavor", searchFlavorTag);

        // then
        assertAll(
                () -> assertThat(matchedCocktails).isNotEmpty(),
                () -> assertThat(matchedCocktails).allMatch(
                        c -> c.getSensoryDescriptors().codeValues(SensoryAxis.AROMA).contains(searchFlavorTag)
                )
        );
    }

    @Test
    @DisplayName("태그로 칵테일 검색 - 매칭되는 칵테일 없음")
    void shouldReturnEmptyListWhenNoMatchingTag() {
        // given
        var nonExistentTag = "존재하지않는태그";

        // when
        var matchedCocktails = cocktailRepository.findByTag("taste", nonExistentTag);

        // then
        assertThat(matchedCocktails).isEmpty();
    }

    @Test
    @DisplayName("칵테일 데이터에 필수 필드가 모두 존재")
    void shouldHaveAllRequiredFields() {
        // when
        var allCocktails = cocktailRepository.findAll();

        // then
        assertThat(allCocktails).isNotEmpty();
        for (Cocktail eachCocktail : allCocktails) {
            assertAll(
                    () -> assertThat(eachCocktail.getId()).isNotNull(),
                    () -> assertThat(eachCocktail.getName()).isNotBlank(),
                    () -> assertThat(eachCocktail.getIngredients()).isNotEmpty(),
                    () -> assertThat(eachCocktail.getRecipe()).isNotBlank(),
                    () -> assertThat(eachCocktail.getSensoryDescriptors().isEmpty()).isFalse()
            );
        }
    }

    @Test
    @DisplayName("descriptors로 칵테일 검색 - 단일 descriptor")
    void shouldFindCocktailsByDescriptor() {
        // given
        var sweetDescriptor = new DescriptorCode(SensoryAxis.TASTE, "sweet");

        // when
        var matchedCocktails = cocktailRepository.findByDescriptors(Set.of(sweetDescriptor));

        // then
        assertAll(
                () -> assertThat(matchedCocktails).isNotEmpty(),
                () -> assertThat(matchedCocktails).allMatch(
                        c -> c.getSensoryDescriptors().taste().contains(sweetDescriptor)
                )
        );
    }

    @Test
    @DisplayName("descriptors로 칵테일 검색 - 복수 descriptors (OR 조건)")
    void shouldFindCocktailsByMultipleDescriptors() {
        // given
        var sweetDescriptor = new DescriptorCode(SensoryAxis.TASTE, "sweet");
        var fruityDescriptor = new DescriptorCode(SensoryAxis.AROMA, "fruity");

        // when
        var matchedCocktails = cocktailRepository.findByDescriptors(Set.of(sweetDescriptor, fruityDescriptor));

        // then
        assertAll(
                () -> assertThat(matchedCocktails).isNotEmpty(),
                () -> assertThat(matchedCocktails).allMatch(c -> {
                    var descriptors = c.getSensoryDescriptors();
                    return descriptors.taste().contains(sweetDescriptor)
                            || descriptors.aroma().contains(fruityDescriptor);
                })
        );
    }

    @Test
    @DisplayName("descriptors로 칵테일 검색 - 매칭되는 칵테일 없음")
    void shouldReturnEmptyListWhenNoMatchingDescriptors() {
        // given
        var nonExistentDescriptor = new DescriptorCode(SensoryAxis.TASTE, "nonexistent");

        // when
        var matchedCocktails = cocktailRepository.findByDescriptors(Set.of(nonExistentDescriptor));

        // then
        assertThat(matchedCocktails).isEmpty();
    }

    @Test
    @DisplayName("descriptors로 칵테일 검색 - 빈 Set으로 검색")
    void shouldReturnEmptyListWhenDescriptorsIsEmpty() {
        // when
        var matchedCocktails = cocktailRepository.findByDescriptors(Set.of());

        // then
        assertThat(matchedCocktails).isEmpty();
    }
}

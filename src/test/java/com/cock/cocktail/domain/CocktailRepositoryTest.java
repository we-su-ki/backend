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
        var allCocktails = cocktailRepository.findAll();

        assertAll(
                () -> assertThat(allCocktails).isNotEmpty(),
                () -> assertThat(allCocktails.size()).isGreaterThanOrEqualTo(10)
        );
    }

    @Test
    @DisplayName("전체 칵테일 조회")
    void shouldReturnAllCocktails() {
        var allCocktails = cocktailRepository.findAll();

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
        var firstCocktail = cocktailRepository.findAll().get(0);
        var existingCocktailId = firstCocktail.getId();

        var foundCocktail = cocktailRepository.findById(existingCocktailId);

        assertAll(
                () -> assertThat(foundCocktail).isPresent(),
                () -> assertThat(foundCocktail.get().getId()).isEqualTo(existingCocktailId),
                () -> assertThat(foundCocktail.get().getName()).isNotBlank()
        );
    }

    @Test
    @DisplayName("ID로 칵테일 조회 - 존재하지 않는 경우")
    void shouldReturnEmptyWhenCocktailNotFound() {
        var nonExistentCocktailId = 999999L;

        var foundCocktail = cocktailRepository.findById(nonExistentCocktailId);

        assertThat(foundCocktail).isEmpty();
    }

    @Test
    @DisplayName("태그로 칵테일 검색 - 맛 태그")
    void shouldFindCocktailsByTasteTag() {
        var searchTasteTag = "sweet";

        var matchedCocktails = cocktailRepository.findByTag("taste", searchTasteTag);

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
        var searchFlavorTag = "fruity";

        var matchedCocktails = cocktailRepository.findByTag("flavor", searchFlavorTag);

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
        var nonExistentTag = "존재하지않는태그";

        var matchedCocktails = cocktailRepository.findByTag("taste", nonExistentTag);

        assertThat(matchedCocktails).isEmpty();
    }

    @Test
    @DisplayName("칵테일 데이터에 필수 필드가 모두 존재")
    void shouldHaveAllRequiredFields() {
        var allCocktails = cocktailRepository.findAll();

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
        var sweetDescriptor = new DescriptorCode(SensoryAxis.TASTE, "sweet");

        var matchedCocktails = cocktailRepository.findByDescriptors(Set.of(sweetDescriptor));

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
        var sweetDescriptor = new DescriptorCode(SensoryAxis.TASTE, "sweet");
        var fruityDescriptor = new DescriptorCode(SensoryAxis.AROMA, "fruity");

        var matchedCocktails = cocktailRepository.findByDescriptors(Set.of(sweetDescriptor, fruityDescriptor));

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
        var nonExistentDescriptor = new DescriptorCode(SensoryAxis.TASTE, "nonexistent");

        var matchedCocktails = cocktailRepository.findByDescriptors(Set.of(nonExistentDescriptor));

        assertThat(matchedCocktails).isEmpty();
    }

    @Test
    @DisplayName("descriptors로 칵테일 검색 - 빈 Set으로 검색")
    void shouldReturnEmptyListWhenDescriptorsIsEmpty() {
        var matchedCocktails = cocktailRepository.findByDescriptors(Set.of());

        assertThat(matchedCocktails).isEmpty();
    }
}

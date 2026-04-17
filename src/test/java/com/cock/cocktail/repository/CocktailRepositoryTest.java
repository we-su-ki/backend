package com.cock.cocktail.repository;

import com.cock.cocktail.domain.descriptor.DescriptorCode;
import com.cock.cocktail.domain.descriptor.SensoryAxis;
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

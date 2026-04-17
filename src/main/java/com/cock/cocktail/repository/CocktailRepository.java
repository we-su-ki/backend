package com.cock.cocktail.repository;

import com.cock.cocktail.domain.Cocktail;
import org.springframework.data.repository.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CocktailRepository extends Repository<Cocktail, Long> {

    @Query("SELECT DISTINCT c FROM Cocktail c JOIN FETCH c.cocktailIngredients ci JOIN FETCH ci.ingredient")
    List<Cocktail> findAll();
}

package com.cock.cocktail.repository;

import com.cock.cocktail.domain.Ingredient;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface IngredientRepository extends Repository<Ingredient, Long> {
    List<Ingredient> findAll();
}

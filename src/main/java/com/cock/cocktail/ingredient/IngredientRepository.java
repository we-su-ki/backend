package com.cock.cocktail.ingredient;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface IngredientRepository extends Repository<Ingredient, Long> {
    List<Ingredient> findAll();
}

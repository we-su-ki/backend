package com.cock.cocktail.repository;

import com.cock.cocktail.domain.Cocktail;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface CocktailRepository extends Repository<Cocktail, String> {

    List<Cocktail> findAll();

    Optional<Cocktail> findByName(String name);
}

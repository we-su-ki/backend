package com.cock.cocktail.repository;

import com.cock.cocktail.domain.Cocktail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CocktailRepository extends Repository<Cocktail, String> {

    List<Cocktail> findAll();

    Optional<Cocktail> findByName(String name);

    @Query("SELECT c FROM Cocktail c WHERE c.id IN :ids")
    List<Cocktail> findAllByIdIn(@Param("ids") List<Long> ids);
}

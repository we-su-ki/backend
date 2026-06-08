package com.cock.cocktail.repository;

import com.cock.cocktail.domain.Cocktail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CocktailRepository extends Repository<Cocktail, String> {

    Page<Cocktail> findAll(Pageable pageable);

    Page<Cocktail> findAllByMethodCategoryIgnoreCase(String methodCategory, Pageable pageable);

    @Query("SELECT c FROM Cocktail c WHERE c.id IN :ids")
    List<Cocktail> findAllByIdIn(@Param("ids") List<Long> ids);
}

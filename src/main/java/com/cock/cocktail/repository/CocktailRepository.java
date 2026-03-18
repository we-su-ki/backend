package com.cock.cocktail.repository;

import com.cock.cocktail.domain.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CocktailRepository extends JpaRepository<Cocktail, Long> {

    @Query("SELECT DISTINCT c FROM Cocktail c JOIN c.cocktailTags t WHERE t.category = :category AND t.tag = :tag")
    List<Cocktail> findByTag(@Param("category") String category, @Param("tag") String tag);
}

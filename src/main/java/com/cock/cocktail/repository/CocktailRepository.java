package com.cock.cocktail.repository;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.DescriptorCode;
import org.springframework.data.repository.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface CocktailRepository extends Repository<Cocktail, Long> {

    @Query("SELECT DISTINCT c FROM Cocktail c JOIN FETCH c.ingredients")
    List<Cocktail> findAll();

    @Query("SELECT DISTINCT c FROM Cocktail c JOIN FETCH c.sensoryDescriptors d WHERE d IN :descriptors")
    List<Cocktail> findByDescriptors(@Param("descriptors") Set<DescriptorCode> descriptors);
}

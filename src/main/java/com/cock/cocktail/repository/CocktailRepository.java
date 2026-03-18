package com.cock.cocktail.repository;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.SensoryAxis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CocktailRepository extends JpaRepository<Cocktail, Long> {

    @Query("SELECT DISTINCT c FROM Cocktail c JOIN c.sensoryDescriptors d WHERE d.axis = :axis AND d.value = :value")
    List<Cocktail> findBySensoryDescriptor(@Param("axis") SensoryAxis axis, @Param("value") String value);

    default List<Cocktail> findByTag(String category, String tag) {
        return findBySensoryDescriptor(toAxis(category), tag);
    }

    private static SensoryAxis toAxis(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("category must not be blank");
        }

        return switch (category.trim().toLowerCase()) {
            case "taste" -> SensoryAxis.TASTE;
            case "flavor", "aroma" -> SensoryAxis.AROMA;
            case "texture", "mouthfeel" -> SensoryAxis.MOUTHFEEL;
            case "carbonation", "sensation" -> SensoryAxis.SENSATION;
            case "mood", "impression" -> SensoryAxis.IMPRESSION;
            default -> throw new IllegalArgumentException("Unknown category: " + category);
        };
    }
}

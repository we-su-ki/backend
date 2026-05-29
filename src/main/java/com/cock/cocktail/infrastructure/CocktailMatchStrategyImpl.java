package com.cock.cocktail.infrastructure;

import com.cock.cocktail.application.CocktailMatchStrategy;
import com.cock.cocktail.domain.taste.TasteMatch;
import com.cock.cocktail.domain.taste.TasteProfile;
import com.cock.cocktail.repository.CocktailRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CocktailMatchStrategyImpl implements CocktailMatchStrategy {

    private static final int MATCH_LIMIT = 5;

    private final CocktailRepository cocktailRepository;
    private final EntityManager entityManager;

    @Override
    public List<TasteMatch> match(TasteProfile query, Boolean isAlcohol) {
        var rows = ((List<Object[]>) entityManager.createNativeQuery(
                "SELECT id, similarity FROM match_cocktails(CAST(:flavor AS vector), :isAlcohol, :matchLimit)"
        )
                .setParameter("flavor", query.toVectorString())
                .setParameter("isAlcohol", toAlcoholInt(isAlcohol))
                .setParameter("matchLimit", MATCH_LIMIT)
                .getResultList())
                .stream().map(MatchRow::from).toList();

        var ids = rows.stream().map(MatchRow::id).toList();
        var scoreById = rows.stream().collect(Collectors.toMap(MatchRow::id, MatchRow::similarity));

        return cocktailRepository.findAllByIdIn(ids).stream()
                .map(c -> new TasteMatch(c, scoreById.getOrDefault(c.getId(), 0.0)))
                .sorted((a, b) -> Double.compare(b.score(), a.score()))
                .toList();
    }

    private record MatchRow(long id, double similarity) {
        static MatchRow from(Object[] row) {
            return new MatchRow(((Number) row[0]).longValue(), ((Number) row[1]).doubleValue());
        }
    }

    private int toAlcoholInt(Boolean isAlcohol) {
        if (isAlcohol == null) return 2;
        return isAlcohol ? 1 : 0;
    }
}

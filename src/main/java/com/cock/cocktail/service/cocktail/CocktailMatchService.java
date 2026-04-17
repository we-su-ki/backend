package com.cock.cocktail.service.cocktail;

import com.cock.cocktail.domain.taste.TasteMatch;
import com.cock.cocktail.domain.taste.TasteQuery;
import com.cock.cocktail.repository.CocktailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CocktailMatchService {

    private final CocktailRepository cocktailRepository;

    public List<TasteMatch> match(TasteQuery query) {
        return cocktailRepository.findAll().stream()
                .map(c -> new TasteMatch(c, c.getTasteProfile().similarity(query)))
                .filter(m -> m.score() > 0)
                .sorted(Comparator.comparingDouble(TasteMatch::score).reversed())
                .limit(5)
                .toList();
    }
}

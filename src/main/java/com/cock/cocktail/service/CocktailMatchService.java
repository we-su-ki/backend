package com.cock.cocktail.service;

import com.cock.cocktail.domain.TasteMatch;
import com.cock.cocktail.domain.TasteQuery;
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

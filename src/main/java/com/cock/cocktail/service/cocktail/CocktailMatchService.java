package com.cock.cocktail.service.cocktail;

import com.cock.cocktail.domain.taste.TasteMatch;
import com.cock.cocktail.domain.taste.TasteQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CocktailMatchService {

    private final CocktailMatchStrategy strategy;

    public List<TasteMatch> match(TasteQuery query) {
        return strategy.match(query);
    }
}

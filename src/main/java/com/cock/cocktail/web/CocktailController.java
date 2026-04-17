package com.cock.cocktail.web;

import com.cock.cocktail.domain.taste.TasteQuery;
import com.cock.cocktail.repository.CocktailRepository;
import com.cock.cocktail.application.CocktailMatchStrategy;
import com.cock.cocktail.web.dto.CocktailListItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cocktails")
@RequiredArgsConstructor
public class CocktailController {

    private final CocktailRepository cocktailRepository;
    private final CocktailMatchStrategy matchStrategy;

    @GetMapping
    public List<CocktailListItemDto> list() {
        return cocktailRepository.findAll().stream()
                .map(CocktailListItemDto::from)
                .toList();
    }

    @GetMapping("/match")
    public List<CocktailListItemDto> match(TasteQuery query) {
        var tasteMatches = matchStrategy.match(query);
        return tasteMatches.stream()
                .map(CocktailListItemDto::from)
                .toList();
    }
}


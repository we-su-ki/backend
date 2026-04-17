package com.cock.cocktail.web;

import com.cock.cocktail.domain.taste.TasteQuery;
import com.cock.cocktail.repository.CocktailRepository;
import com.cock.cocktail.service.cocktail.CocktailMatchService;
import com.cock.cocktail.web.dto.CocktailListItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cocktails")
@RequiredArgsConstructor
public class CocktailController {

    private final CocktailRepository cocktailRepository;
    private final CocktailMatchService matchService;

    @GetMapping
    public List<CocktailListItemDto> list() {
        return cocktailRepository.findAll().stream()
                .map(CocktailListItemDto::from)
                .toList();
    }

    @GetMapping("/match")
    public List<CocktailListItemDto> match(TasteQuery query) {
        return matchService.match(query).stream()
                .map(m -> CocktailListItemDto.from(m.cocktail(), m.score()))
                .toList();
    }
}


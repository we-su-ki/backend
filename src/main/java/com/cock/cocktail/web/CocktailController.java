package com.cock.cocktail.web;

import com.cock.cocktail.application.CocktailMatchStrategy;
import com.cock.cocktail.application.TasteProfileTranslator;
import com.cock.cocktail.domain.taste.TasteMatch;
import com.cock.cocktail.repository.CocktailRepository;
import com.cock.cocktail.web.dto.CocktailListItemDto;
import com.cock.cocktail.web.dto.RecommendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cocktails")
@RequiredArgsConstructor
public class CocktailController {

    private final CocktailRepository cocktailRepository;
    private final CocktailMatchStrategy matchStrategy;
    private final TasteProfileTranslator tasteProfileTranslator;

    // TODO: 페이지네이션
    @GetMapping
    public List<CocktailListItemDto> list() {
        return cocktailRepository.findAll().stream()
                .map(CocktailListItemDto::from)
                .toList();
    }

    @PostMapping("/recommend")
    public List<CocktailListItemDto> recommend(@RequestBody RecommendRequest request) {
        var tasteProfile = tasteProfileTranslator.translate(request.query());
        var recommendedCocktails = matchStrategy.match(tasteProfile, request.isAlcohol());
        return recommendedCocktails.stream()
                .map(CocktailListItemDto::from)
                .toList();
    }
}

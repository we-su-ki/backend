package com.cock.cocktail.web;

import com.cock.cocktail.domain.TasteQuery;
import com.cock.cocktail.repository.CocktailRepository;
import com.cock.cocktail.service.CocktailMatchService;
import com.cock.cocktail.service.CocktailRecommendationService;
import com.cock.cocktail.service.KeywordAnalyzer;
import com.cock.cocktail.web.dto.CocktailListItemDto;
import com.cock.cocktail.web.dto.RecommendedCocktailDto;
import com.cock.cocktail.web.dto.SensoryDescriptorsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cocktails")
@RequiredArgsConstructor
public class CocktailController {

    private final CocktailRepository cocktailRepository;
    private final KeywordAnalyzer keywordAnalyzer;
    private final CocktailRecommendationService recommendationService;
    private final CocktailMatchService matchService;

    @GetMapping
    public List<CocktailListItemDto> list() {
        return cocktailRepository.findAll().stream()
                .map(CocktailListItemDto::from)
                .toList();
    }

    @GetMapping("/analyze")
    public SensoryDescriptorsDto analyze(@RequestParam String query) {
        return SensoryDescriptorsDto.from(keywordAnalyzer.analyze(query));
    }

    @GetMapping("/recommend")
    public List<RecommendedCocktailDto> recommend(@RequestParam String query) {
        return RecommendedCocktailDto.from(recommendationService.recommend(query));
    }

    @GetMapping("/match")
    public List<CocktailListItemDto> match(TasteQuery query) {
        return matchService.match(query).stream()
                .map(m -> CocktailListItemDto.from(m.cocktail(), m.score()))
                .toList();
    }
}


package com.cock.cocktail.web;

import com.cock.cocktail.service.CocktailRecommendationService;
import com.cock.cocktail.service.KeywordAnalyzer;
import com.cock.cocktail.web.dto.CocktailRecommendationResponse;
import com.cock.cocktail.web.dto.RecommendedCocktailDto;
import com.cock.cocktail.web.dto.SensoryDescriptorsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cocktails")
@RequiredArgsConstructor
public class CocktailController {

    private final KeywordAnalyzer keywordAnalyzer;
    private final CocktailRecommendationService recommendationService;

    @GetMapping("/analyze")
    public SensoryDescriptorsDto analyze(@RequestParam String query) {
        var sensoryDescriptors = keywordAnalyzer.analyze(query);
        return SensoryDescriptorsDto.from(sensoryDescriptors);
    }

    @GetMapping("/recommend")
    public CocktailRecommendationResponse recommend(@RequestParam String query) {
        var matches = recommendationService.recommend(query);
        var dtos = RecommendedCocktailDto.from(matches);
        return CocktailRecommendationResponse.from(dtos);
    }
}


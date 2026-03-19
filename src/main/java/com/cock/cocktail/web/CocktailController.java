package com.cock.cocktail.web;

import com.cock.cocktail.service.KeywordAnalyzer;
import com.cock.cocktail.web.dto.SensoryDescriptorsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cocktails")
@RequiredArgsConstructor
public class CocktailController {

    private final KeywordAnalyzer keywordAnalyzer;

    @GetMapping("/analyze")
    public SensoryDescriptorsDto analyze(@RequestParam String query) {
        var sensoryDescriptors = keywordAnalyzer.analyze(query);
        return SensoryDescriptorsDto.from(sensoryDescriptors);
    }
}

package com.cock.cocktail.web;

import com.cock.cocktail.application.CocktailMatchStrategy;
import com.cock.cocktail.application.TasteProfileTranslator;
import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.taste.TasteMatch;
import com.cock.cocktail.repository.CocktailRepository;
import com.cock.cocktail.web.dto.CocktailListItemDto;
import com.cock.cocktail.web.dto.PageResponse;
import com.cock.cocktail.web.dto.RecommendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cocktails")
@RequiredArgsConstructor
public class CocktailController {

    private final CocktailRepository cocktailRepository;
    private final CocktailMatchStrategy matchStrategy;
    private final TasteProfileTranslator tasteProfileTranslator;

    @GetMapping
    public PageResponse<CocktailListItemDto> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String method
    ) {
        var validPage = Math.max(page, 0);
        var validSize = size <= 0 ? 20 : size;
        var pageable = PageRequest.of(validPage, validSize);

        Page<Cocktail> cocktailPage = (method != null && !method.isBlank())
                ? cocktailRepository.findAllByMethodCategoryIgnoreCase(method, pageable)
                : cocktailRepository.findAll(pageable);

        return PageResponse.from(cocktailPage.map(CocktailListItemDto::from));
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

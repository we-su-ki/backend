package com.cock.cocktail.web;

import com.cock.cocktail.domain.taste.TasteQuery;
import com.cock.cocktail.repository.CocktailRepository;
import com.cock.cocktail.application.CocktailMatchStrategy;
import com.cock.cocktail.application.AIService;
import com.cock.cocktail.web.dto.CocktailListItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cocktails")
@RequiredArgsConstructor
public class CocktailController {

    private final AIService aiService;
    private final CocktailRepository cocktailRepository;
    private final CocktailMatchStrategy matchStrategy;

    // TODO: 페이지네이션
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

    // 사용자가 재료를 담아 '맛 예측'을 누르면 실행됨
    @PostMapping("/predict")
    public ResponseEntity<Map<String, Object>> predict(@RequestBody Map<String, Object> payload) {
        // payload 자체가 {"ingredients": [{id:1, amount:30}, ...]} 형태라면 바로 전달
        List<Map<String, Object>> ingredients = (List<Map<String, Object>>) payload.get("ingredients");
        return ResponseEntity.ok(aiService.getFlavorPrediction(ingredients));
    }
}


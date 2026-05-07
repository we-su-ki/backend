package com.cock.cocktail.web;

import com.cock.cocktail.repository.IngredientRepository;
import com.cock.cocktail.application.FlavorVectorResolver;
import com.cock.cocktail.web.dto.IngredientDto;
import com.cock.cocktail.web.dto.PredictRequest;
import com.cock.cocktail.web.dto.TasteProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientRepository ingredientRepository;
    private final FlavorVectorResolver flavorVectorResolver;

    @GetMapping
    public List<IngredientDto> list() {
        return ingredientRepository.findAll().stream()
                .map(IngredientDto::from)
                .toList();
    }

    @PostMapping("/predict")
    public TasteProfileDto predict(@RequestBody PredictRequest request) {
        var tasteProfile = flavorVectorResolver.resolve(request.ingredients());
        return TasteProfileDto.from(tasteProfile);
    }
}

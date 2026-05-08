package com.cock.cocktail.web;

import com.cock.cocktail.repository.IngredientRepository;
import com.cock.cocktail.application.TasteProfilePredictor;
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
    private final TasteProfilePredictor tasteProfilePredictor;

    @GetMapping
    public List<IngredientDto> list() {
        return ingredientRepository.findAll().stream()
                .map(IngredientDto::from)
                .toList();
    }

    @PostMapping("/predict")
    public TasteProfileDto predict(@RequestBody PredictRequest request) {
        var tasteProfile = tasteProfilePredictor.predict(request.ingredients(), request.methodCategory());
        return TasteProfileDto.from(tasteProfile);
    }
}

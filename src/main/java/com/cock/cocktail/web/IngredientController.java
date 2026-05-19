package com.cock.cocktail.web;

import com.cock.cocktail.repository.IngredientRepository;
import com.cock.cocktail.application.TasteProfilePredictor;
import com.cock.cocktail.web.dto.IngredientDto;
import com.cock.cocktail.web.dto.PredictRequest;
import com.cock.cocktail.web.dto.PredictResultDto;
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
    public PredictResultDto predict(@RequestBody PredictRequest request) {
        return PredictResultDto.from(tasteProfilePredictor.predict(request.ingredients(), request.methodCategory()));
    }
}

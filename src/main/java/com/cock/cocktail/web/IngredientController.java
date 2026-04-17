package com.cock.cocktail.web;

import com.cock.cocktail.repository.IngredientRepository;
import com.cock.cocktail.web.dto.IngredientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    @GetMapping
    public List<IngredientDto> list() {
        return ingredientRepository.findAll().stream()
                .map(IngredientDto::from)
                .toList();
    }
}

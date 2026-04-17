package com.cock.cocktail.web;

import com.cock.cocktail.domain.taste.TasteProfile;
import com.cock.cocktail.service.FlavorVectorResolver;
import com.cock.cocktail.web.dto.PredictRequest;
import com.cock.cocktail.web.dto.TasteProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/predict")
@RequiredArgsConstructor
public class TastePredictController {

    private final FlavorVectorResolver flavorVectorResolver;

    @PostMapping
    public TasteProfileDto predict(@RequestBody PredictRequest request) {
        var flavorVector = flavorVectorResolver.resolve(request.ingredients());
        var tasteProfile = flavorVector.computeTasteProfile();
        return TasteProfileDto.from(tasteProfile);
    }
}

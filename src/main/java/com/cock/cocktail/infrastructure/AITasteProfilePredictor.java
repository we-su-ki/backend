package com.cock.cocktail.infrastructure;

import com.cock.cocktail.application.IngredientAmount;
import com.cock.cocktail.application.TasteProfilePrediction;
import com.cock.cocktail.application.TasteProfilePredictor;
import com.cock.cocktail.domain.MethodCategory;
import com.cock.cocktail.domain.taste.TasteProfile;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AITasteProfilePredictor implements TasteProfilePredictor {

    private final RestTemplate restTemplate;

    @Value("${api.flavor-resolver.url}")
    private String predictorEndpoint;

    @Override
    public TasteProfilePrediction predict(List<IngredientAmount> ingredients, MethodCategory methodCategory) {
        var request = Map.of("ingredients", ingredients, "methodCategory", methodCategory.canonicalName());
        var attributes = restTemplate.postForObject(predictorEndpoint, request, TasteProfileAttributes.class);
        if (attributes == null) throw new NullPointerException("맛 예측 API의 응답이 null입니다.");

        var prediction = attributes.toPrediction();
        log.info("AI 맛 예측 - ingredients: {}, methodCategory: '{}' -> 결과: {}", ingredients, methodCategory, prediction);
        return prediction;
    }

    private record TasteProfileAttributes(
            @JsonProperty("Abv")         double abv,
            @JsonProperty("Sweet")       double sweetness,
            @JsonProperty("Sour")        double sourness,
            @JsonProperty("Bitter")      double bitterness,
            @JsonProperty("Umami_Salty") double umamiSalty,
            @JsonProperty("Fruity")      double fruity,
            @JsonProperty("Citrus")      double citrus,
            @JsonProperty("Floral")      double floral,
            @JsonProperty("Herbal")      double herbal,
            @JsonProperty("Spicy")       double spicy,
            @JsonProperty("Woody_Smoky") double woodySmoky,
            @JsonProperty("Body")        double body,
            @JsonProperty("Fizzy")       double fizzy
    ) {
        TasteProfilePrediction toPrediction() {
            var tasteProfile = TasteProfile.builder()
                    .sweetness(sweetness)
                    .sourness(sourness)
                    .bitterness(bitterness)
                    .umamiSalty(umamiSalty)
                    .fruity(fruity)
                    .citrus(citrus)
                    .floral(floral)
                    .herbal(herbal)
                    .spicy(spicy)
                    .woodySmoky(woodySmoky)
                    .body(body)
                    .fizzy(fizzy)
                    .build();
            return new TasteProfilePrediction(tasteProfile, abv);
        }
    }
}

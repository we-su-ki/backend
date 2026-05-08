package com.cock.cocktail.infrastructure;

import com.cock.cocktail.application.TasteProfilePredictor;
import com.cock.cocktail.application.IngredientAmount;
import com.cock.cocktail.domain.taste.TasteProfile;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AITasteProfilePredictor implements TasteProfilePredictor {

    private final RestTemplate restTemplate;

    @Value("${api.flavor-resolver.url}")
    private String predictorEndpoint;

    @Override
    public TasteProfile predict(List<IngredientAmount> ingredients) {
        var request = Map.of("ingredients", ingredients);
        var attributes = restTemplate.postForObject(predictorEndpoint, request, TasteProfileAttributes.class);
        return attributes == null ? TasteProfile.empty() : attributes.toTasteProfile();
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
        TasteProfile toTasteProfile() {
            return TasteProfile.builder()
                    .abv(abv)
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
        }
    }
}

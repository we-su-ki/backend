package com.cock.cocktail.infrastructure;

import com.cock.cocktail.domain.taste.FlavorVector;
import com.cock.cocktail.application.FlavorVectorResolver;
import com.cock.cocktail.domain.IngredientAmount;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StubFlavorVectorResolver implements FlavorVectorResolver {

    @Override
    public FlavorVector resolve(List<IngredientAmount> ingredients) {
        return new FlavorVector();
    }
}

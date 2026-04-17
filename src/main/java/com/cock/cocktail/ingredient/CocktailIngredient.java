package com.cock.cocktail.ingredient;

import com.cock.cocktail.domain.Cocktail;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CocktailIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cocktail_id")
    private Cocktail cocktail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private int amount;

    public CocktailIngredient(Cocktail cocktail, Ingredient ingredient, int amount) {
        this.cocktail = cocktail;
        this.ingredient = ingredient;
        this.amount = amount;
    }
}

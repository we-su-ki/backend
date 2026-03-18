package com.cock.cocktail.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CocktailDto {

    private String id;
    private String name;
    private List<Ingredient> ingredients;
    private List<String> recipe;
    private String reason;
    private List<String> tags;
}

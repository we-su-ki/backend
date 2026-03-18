package com.cock.cocktail.model.domain;

import com.cock.cocktail.model.dto.Ingredient;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Cocktail {

    private String id;
    private String name;
    private List<Ingredient> ingredients;
    private List<String> recipe;
    private Map<String, List<String>> tags;
}

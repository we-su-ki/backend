package com.cock.cocktail.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class Cocktail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "cocktail_ingredients", joinColumns = @JoinColumn(name = "cocktail_id"))
    private List<Ingredient> ingredients = new ArrayList<>();

    @Column(length = 1000)
    private String recipe;

    @ElementCollection
    @CollectionTable(name = "cocktail_tags", joinColumns = @JoinColumn(name = "cocktail_id"))
    private List<CocktailTag> cocktailTags = new ArrayList<>();

    public Map<String, List<String>> getTags() {
        return cocktailTags.stream()
                .collect(Collectors.groupingBy(
                        CocktailTag::category,
                        Collectors.mapping(CocktailTag::tag, Collectors.toList())
                ));
    }
}

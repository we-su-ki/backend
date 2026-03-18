package com.cock.cocktail.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
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
    private List<Ingredient> ingredients;

    @Column(length = 1000)
    private String recipe;

    @ElementCollection
    @CollectionTable(name = "cocktail_tags", joinColumns = @JoinColumn(name = "cocktail_id"))
    private List<CocktailTag> cocktailTags;

    // 비즈니스 로직을 위한 헬퍼 메서드
    public Map<String, List<String>> getTags() {
        if (cocktailTags == null) {
            return new HashMap<>();
        }
        return cocktailTags.stream()
                .collect(Collectors.groupingBy(
                        CocktailTag::category,
                        Collectors.mapping(CocktailTag::tag, Collectors.toList())
                ));
    }

    public void setCocktailTags(List<CocktailTag> cocktailTags) {
        this.cocktailTags = cocktailTags;
    }
}

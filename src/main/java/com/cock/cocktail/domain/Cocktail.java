package com.cock.cocktail.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
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

    @ElementCollection
    @CollectionTable(name = "cocktail_recipe", joinColumns = @JoinColumn(name = "cocktail_id"))
    @Column(name = "step")
    private List<String> recipe;

    @ElementCollection
    @CollectionTable(name = "cocktail_tags", joinColumns = @JoinColumn(name = "cocktail_id"))
    private List<CocktailTag> cocktailTags;

    // JSON 직렬화/역직렬화를 위한 헬퍼 메서드
    @JsonProperty("tags")
    public void setTagsFromMap(Map<String, List<String>> tagsMap) {
        if (tagsMap == null) {
            this.cocktailTags = new ArrayList<>();
            return;
        }
        this.cocktailTags = tagsMap.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(tag -> new CocktailTag(entry.getKey(), tag)))
                .toList();
    }

    @JsonProperty("tags")
    public Map<String, List<String>> getTagsAsMap() {
        if (cocktailTags == null) {
            return new HashMap<>();
        }
        return cocktailTags.stream()
                .collect(Collectors.groupingBy(
                        CocktailTag::category,
                        Collectors.mapping(CocktailTag::tag, Collectors.toList())
                ));
    }

    // 테스트 및 비즈니스 로직을 위한 메서드
    public Map<String, List<String>> getTags() {
        return getTagsAsMap();
    }
}

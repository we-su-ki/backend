package com.cock.cocktail.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    @CollectionTable(name = "cocktail_sensory_descriptors", joinColumns = @JoinColumn(name = "cocktail_id"))
    private List<DescriptorCode> sensoryDescriptors = new ArrayList<>();

    public SensoryDescriptors getSensoryDescriptors() {
        return new SensoryDescriptors(sensoryDescriptors);
    }
}

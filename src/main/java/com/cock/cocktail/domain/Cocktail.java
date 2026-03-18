package com.cock.cocktail.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cocktail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "cocktail_ingredients", joinColumns = @JoinColumn(name = "cocktail_id"))
    @Builder.Default
    private List<Ingredient> ingredients = new ArrayList<>();

    @Column(length = 1000)
    private String recipe;

    @ElementCollection
    @CollectionTable(name = "cocktail_sensory_descriptors", joinColumns = @JoinColumn(name = "cocktail_id"))
    @Builder.Default
    private List<DescriptorCode> sensoryDescriptors = new ArrayList<>();

    public SensoryDescriptors getSensoryDescriptors() {
        return new SensoryDescriptors(sensoryDescriptors);
    }
}

package com.cock.cocktail.domain;

import com.cock.cocktail.domain.descriptor.DescriptorCode;
import com.cock.cocktail.domain.descriptor.SensoryDescriptors;
import com.cock.cocktail.domain.taste.FlavorVector;
import com.cock.cocktail.domain.taste.TasteProfile;
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

    private String imageUrl;

    @Embedded
    @Builder.Default
    private FlavorVector flavorVector = new FlavorVector();

    @OneToMany(mappedBy = "cocktail")
    @Builder.Default
    private List<CocktailIngredient> cocktailIngredients = new ArrayList<>();

    @Column(length = 1000)
    private String recipe;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "cocktail_sensory_descriptors", joinColumns = @JoinColumn(name = "cocktail_id"))
    @Builder.Default
    private List<DescriptorCode> sensoryDescriptors = new ArrayList<>();

    public SensoryDescriptors getSensoryDescriptors() {
        return new SensoryDescriptors(sensoryDescriptors);
    }

    public TasteProfile getTasteProfile() {
        return flavorVector.computeTasteProfile();
    }
}

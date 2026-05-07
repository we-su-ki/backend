package com.cock.cocktail.domain;

import com.cock.cocktail.domain.taste.FlavorVector;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredients")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Ingredient {

    @Id
    @Column(name = "ingredient_id")
    private Long id;

    @Column(name = "name_eng")
    private String name;

    @Column(name = "category")
    private String category;

    @Embedded
    @Builder.Default
    private FlavorVector flavorVector = new FlavorVector();
}

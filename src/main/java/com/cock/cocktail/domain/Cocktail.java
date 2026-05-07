package com.cock.cocktail.domain;

import com.cock.cocktail.domain.taste.TasteProfile;
import com.cock.cocktail.infrastructure.CocktailIngredientListConverter;
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
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "glass_raw")
    private String glassRaw;

    @Column(name = "garnish_raw")
    private String garnishRaw;

    @Column(name = "method_raw")
    private String methodRaw;

    @Column(name = "method_category")
    private String methodCategory;

    @Convert(converter = CocktailIngredientListConverter.class)
    @Column(name = "ingredients_ml", columnDefinition = "jsonb")
    @Builder.Default
    private List<CocktailIngredient> ingredients = new ArrayList<>();

    @Column(name = "score_strength")
    private Long scoreStrength;

    @Column(name = "score_sweet_sour")
    private Long scoreSweetSour;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "source_url")
    private String sourceUrl;

    // DB 컬럼(target_*)이 추가되면 @Transient 제거 후 @Embedded + @AttributeOverrides 적용
    @Transient
    @Builder.Default
    private TasteProfile tasteProfile = TasteProfile.empty();
}

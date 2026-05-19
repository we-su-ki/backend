package com.cock.cocktail.domain;

import com.cock.cocktail.domain.taste.TasteProfile;
import com.cock.cocktail.infrastructure.CocktailIngredientListConverter;
import jakarta.persistence.*;
import org.hibernate.type.NumericBooleanConverter;
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
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "glass_raw")
    private String glassRaw;

    @Column(name = "garnish_raw")
    private String garnishRaw;

    @Column(name = "method_category")
    private String methodCategory;

    // TODO: 컬럼 생기면 활성화
    @Transient
    private String methodRaw = "";

    // 알코올 함유 여부 (1 = 알코올 , 0 = 논알콜)
    @Convert(converter = NumericBooleanConverter.class)
    @Column(name = "is_alcohol")
    private Boolean isAlcohol;

    // 칵테일 1잔에 포함된 순수 알코올 중량 (g)
    @Column(name = "pure_alcohol_grams")
    private Double pureAlcoholGrams;

    // 실제 알코올 도수 수치 (proof)
    @Column(name = "proof_inside_bracket_proof")
    private Double proofInsideBracketProof;

    @Convert(converter = CocktailIngredientListConverter.class)
    @Column(name = "ingredients_ml", columnDefinition = "jsonb")
    @Builder.Default
    private List<CocktailIngredient> ingredients = new ArrayList<>();

    @Column(name = "score_strength")
    private Double scoreStrength;

    @Column(name = "score_sweet_sour")
    private Double scoreSweetSour;

    // TODO: 컬럼 생기면 활성화
    @Transient
    private String imageUrl = "https://...";

    @Column(name = "review_text")
    private String reviewText;

    @Embedded
    @Builder.Default
    private TasteProfile tasteProfile = TasteProfile.empty();
}

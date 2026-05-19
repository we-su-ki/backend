package com.cock.cocktail.domain;

import com.cock.cocktail.domain.taste.TasteProfile;
import com.cock.cocktail.infrastructure.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.type.NumericBooleanConverter;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ingredients")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Ingredient {

    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "role")
    private String role;

    // 알코올 함유 여부 (1 = 알코올 , 0 = 논알콜)
    @Convert(converter = NumericBooleanConverter.class)
    @Column(name = "is_alcohol")
    private Boolean isAlcohol;

    // 재료별 중요도 ( 자주 쓰이는 재료 기준 )
    @Column(name = "tier")
    private Long tier;

    @Column(name = "frequency")
    private Long frequency;

    @Convert(converter = StringListConverter.class)
    @Column(name = "flavor_tags", columnDefinition = "jsonb")
    @Builder.Default
    private List<String> flavorTags = new ArrayList<>();

    @Embedded
    @Builder.Default
    private TasteProfile tasteProfile = TasteProfile.empty();
}

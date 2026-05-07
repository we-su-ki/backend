package com.cock.cocktail.domain.taste;

import jakarta.persistence.Embeddable;
import lombok.Builder;

@Embeddable
@Builder
public record TasteProfile(
        Double abv,          // 알코올 도수 (%)
        Double sweetness,    // 단맛 (0~10)
        Double sourness,     // 신맛 (0~10)
        Double bitterness,   // 쓴맛 (0~10)
        Double umamiSalty,   // 감칠/짠맛 (0~10)
        Double fruity,       // 과일향 (0~10)
        Double citrus,       // 시트러스향 (0~10)
        Double floral,       // 꽃향 (0~10)
        Double herbal,       // 허브/풀향 (0~10)
        Double spicy,        // 향신료향 (0~10)
        Double woodySmoky,   // 나무/스모키향 (0~10)
        Double body,         // 바디감/꾸덕함 (0~10)
        Double fizzy         // 청량감 (0~10)
) {
    public static TasteProfile empty() {
        return new TasteProfile(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    public boolean isEmpty() {
        return abv == null
                && sweetness == null
                && sourness == null
                && bitterness == null
                && umamiSalty == null
                && fruity == null
                && citrus == null
                && floral == null
                && herbal == null
                && spicy == null
                && woodySmoky == null
                && body == null
                && fizzy == null;
    }
}

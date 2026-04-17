package com.cock.cocktail.domain.descriptor;

/**
 * 감각 속성의 축
 *
 * 맛 인지는 여러 감각 차원의 결합이므로, 각 축을 분리하여 관리합니다.
 * - TASTE: 기본미 (sweet, sour, bitter, salty)
 * - AROMA: 향 (fruity, smoky, herbal) - 계층 구조 가능
 * - MOUTHFEEL: 질감 (smooth, creamy, light)
 * - SENSATION: 자극감 (carbonated, tingling, warming)
 * - IMPRESSION: 복합 인상 (refreshing, elegant) - 감각 데이터로 직접 환원 불가
 */
public enum SensoryAxis {
    /**
     * 기본미 - 단맛, 신맛, 쓴맛 등
     * 측정: 강도 값 (1-5)
     */
    TASTE,

    /**
     * 향 - 과일향, 허브향, 훈연향 등
     * 측정: 존재 여부, 계층 구조 (fruity > citrus > lime)
     */
    AROMA,

    /**
     * 질감 - 부드러움, 크리미함, 가벼움 등
     * 측정: 존재 여부 또는 강도
     */
    MOUTHFEEL,

    /**
     * 자극감 - 탄산, 따끔함, 온열감 등
     * 측정: 존재 여부 또는 강도
     */
    SENSATION,

    /**
     * 인상/분위기 - 상쾌함, 우아함, 마시기 편함 등
     * 복합적 인상으로, 감각 데이터의 조합으로 해석됨
     */
    IMPRESSION
}

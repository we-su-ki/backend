package com.cock.cocktail.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertAll;

class TasteProfileTest {

    private static final TasteProfile PROFILE = new TasteProfile(4.0, 3.0, 2.0, 1.0, 0.5, 1.5);

    @Test
    @DisplayName("완전히 일치하면 유사도 1.0")
    void perfectMatch() {
        var query = new TasteQuery(4.0, 3.0, 2.0, 1.0, 0.5, 1.5);
        assertThat(PROFILE.similarity(query)).isEqualTo(1.0);
    }

    @Test
    @DisplayName("빈 쿼리면 유사도 0.0")
    void emptyQuery() {
        var query = new TasteQuery(null, null, null, null, null, null);
        assertThat(PROFILE.similarity(query)).isEqualTo(0.0);
    }

    @Test
    @DisplayName("지정된 축만 유사도 계산에 반영")
    void partialAxes() {
        var queryFull = new TasteQuery(4.0, null, null, null, null, null);
        var queryOpposite = new TasteQuery(0.0, null, null, null, null, null);

        assertAll(
                () -> assertThat(PROFILE.similarity(queryFull)).isEqualTo(1.0),
                () -> assertThat(PROFILE.similarity(queryOpposite)).isCloseTo(0.2, within(0.01))
        );
    }

    @Test
    @DisplayName("유사도는 0.0 ~ 1.0 범위")
    void scoreInRange() {
        var worst = new TasteQuery(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        var best = new TasteQuery(4.0, 3.0, 2.0, 1.0, 0.5, 1.5);

        assertAll(
                () -> assertThat(PROFILE.similarity(worst)).isBetween(0.0, 1.0),
                () -> assertThat(PROFILE.similarity(best)).isBetween(0.0, 1.0)
        );
    }

    @Test
    @DisplayName("거리가 가까울수록 유사도가 높음")
    void closerMeansHigherScore() {
        var close = new TasteQuery(4.0, null, null, null, null, null);
        var far = new TasteQuery(0.0, null, null, null, null, null);

        assertThat(PROFILE.similarity(close)).isGreaterThan(PROFILE.similarity(far));
    }
}

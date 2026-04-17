package com.cock.cocktail.service.cocktail;

import com.cock.cocktail.domain.MatchedCocktail;
import com.cock.cocktail.domain.descriptor.SensoryDescriptors;

import java.util.List;

/**
 * 감각 descriptor 기반 칵테일 매칭 서비스
 * <p>
 * SensoryDescriptors를 입력으로 받아 DB에서 유사한 칵테일을 검색하고
 * 매칭 점수를 계산하여 추천 목록을 반환합니다.
 */
public interface CocktailMatcher {

    /**
     * 주어진 sensory descriptors와 매칭되는 칵테일을 검색합니다.
     *
     * @param descriptors 사용자 쿼리에서 추출된 감각 descriptor
     * @return 매칭된 칵테일 목록 (점수 내림차순 정렬)
     */
    List<MatchedCocktail> match(SensoryDescriptors descriptors);
}

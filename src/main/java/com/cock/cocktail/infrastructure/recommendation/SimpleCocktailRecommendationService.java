package com.cock.cocktail.infrastructure.recommendation;

import com.cock.cocktail.domain.MatchedCocktail;
import com.cock.cocktail.service.CocktailMatcher;
import com.cock.cocktail.service.CocktailRecommendationService;
import com.cock.cocktail.service.KeywordAnalyzer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 칵테일 추천 서비스 구현
 * <p>
 * KeywordAnalyzer와 CocktailMatcher를 조합하여
 * 전체 추천 프로세스를 수행합니다.
 */
@Service
@RequiredArgsConstructor
public class SimpleCocktailRecommendationService implements CocktailRecommendationService {

    private final KeywordAnalyzer keywordAnalyzer;
    private final CocktailMatcher cocktailMatcher;

    @Override
    public List<MatchedCocktail> recommend(String query) {
        // 1. 입력 검증
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("query must not be blank");
        }

        // 2. 키워드 분석
        var descriptors = keywordAnalyzer.analyze(query);

        // 3. 칵테일 매칭
        var matches = cocktailMatcher.match(descriptors);

        // 4. 결과 반환
        return matches;
    }
}

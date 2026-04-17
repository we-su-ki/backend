package com.cock.cocktail.infrastructure.recommendation;

import com.cock.cocktail.domain.MatchedCocktail;
import com.cock.cocktail.service.cocktail.CocktailMatcher;
import com.cock.cocktail.service.cocktail.CocktailRecommendationService;
import com.cock.cocktail.service.KeywordAnalyzer;
import com.cock.cocktail.util.Strings;
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
        Strings.requireNotBlank(query, "query must not be blank");

        var descriptors = keywordAnalyzer.analyze(query);
        return cocktailMatcher.match(descriptors);
    }
}

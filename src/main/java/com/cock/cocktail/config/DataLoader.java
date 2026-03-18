package com.cock.cocktail.config;

import com.cock.cocktail.domain.Cocktail;
import com.cock.cocktail.domain.CocktailTag;
import com.cock.cocktail.repository.CocktailRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final CocktailRepository cocktailRepository;
    private final ObjectMapper objectMapper;

    public DataLoader(CocktailRepository cocktailRepository) {
        this.cocktailRepository = cocktailRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void run(String... args) throws Exception {
        if (cocktailRepository.count() == 0) {
            List<Cocktail> cocktails = loadCocktails();
            cocktailRepository.saveAll(cocktails);
        }
    }

    private List<Cocktail> loadCocktails() {
        try {
            ClassPathResource resource = new ClassPathResource("data/cocktails.json");
            List<CocktailDataDto> dataList = objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<List<CocktailDataDto>>() {}
            );
            return dataList.stream()
                    .map(this::convertToEntity)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load cocktail data", e);
        }
    }

    private Cocktail convertToEntity(CocktailDataDto dto) {
        Cocktail cocktail = new Cocktail();
        cocktail.setName(dto.name());
        cocktail.setIngredients(dto.ingredients());
        cocktail.setRecipe(dto.recipe());

        // Map<String, List<String>> tags를 List<CocktailTag>로 변환
        List<CocktailTag> cocktailTags = dto.tags().entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(tag -> new CocktailTag(entry.getKey(), tag)))
                .toList();
        cocktail.setCocktailTags(cocktailTags);

        return cocktail;
    }
}

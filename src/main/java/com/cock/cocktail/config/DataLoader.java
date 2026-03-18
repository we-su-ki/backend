package com.cock.cocktail.config;

import com.cock.cocktail.domain.Cocktail;
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
            return objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<List<Cocktail>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load cocktail data", e);
        }
    }
}

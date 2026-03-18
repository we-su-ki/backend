package com.cock.cocktail.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class CocktailRepository {

    private final List<Cocktail> cocktails;
    private final ObjectMapper objectMapper;

    public CocktailRepository() {
        this.objectMapper = new ObjectMapper();
        this.cocktails = loadCocktails();
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

    public List<Cocktail> findAll() {
        return cocktails;
    }

    public Optional<Cocktail> findById(String id) {
        return cocktails.stream()
                .filter(cocktail -> cocktail.getId().equals(id))
                .findFirst();
    }

    public List<Cocktail> findByTag(String category, String tag) {
        return cocktails.stream()
                .filter(cocktail -> {
                    List<String> tags = cocktail.getTags().get(category);
                    return tags != null && tags.contains(tag);
                })
                .toList();
    }
}

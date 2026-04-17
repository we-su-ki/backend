package com.cock.cocktail.domain.taste;

public record TasteQuery(
        Double sweet,
        Double body,
        Double bitter,
        Double abv,
        Double smoky,
        Double sour
) {
    public boolean isEmpty() {
        return sweet == null && body == null && bitter == null
                && abv == null && smoky == null && sour == null;
    }
}

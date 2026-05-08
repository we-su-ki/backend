package com.cock.cocktail.application;

import com.cock.cocktail.domain.taste.TasteProfile;

public interface TasteProfileTranslator {

    TasteProfile translate(String query);
}

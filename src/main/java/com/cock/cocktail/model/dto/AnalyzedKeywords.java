package com.cock.cocktail.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AnalyzedKeywords {

    private List<String> taste = new ArrayList<>();
    private List<String> texture = new ArrayList<>();
    private List<String> carbonation = new ArrayList<>();
    private List<String> flavor = new ArrayList<>();
    private List<String> mood = new ArrayList<>();
}

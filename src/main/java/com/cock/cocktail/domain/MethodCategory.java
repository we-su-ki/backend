package com.cock.cocktail.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum MethodCategory {
    NONE(""),
    Blend("Blend"),
    Build("Build"),
    Float("Float"),
    Shake("Shake"),
    Stir("Stir");

    private final String canonicalName;

    MethodCategory(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    public String canonicalName() {
        return canonicalName;
    }

    @JsonCreator
    public static MethodCategory fromString(String value) {
        return Arrays.stream(values())
                .filter(mc -> mc.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(NONE);
    }
}

package com.cock.cocktail.util;

public final class Strings {

    private Strings() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    public static String requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static String trimAndLowerCase(String value) {
        return value.trim().toLowerCase();
    }
}

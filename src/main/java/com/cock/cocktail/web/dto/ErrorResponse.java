package com.cock.cocktail.web.dto;

public record ErrorResponse(
        String error,
        String message,
        String timestamp
) {
}

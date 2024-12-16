package com.example.demo.DTOs;

public class CategorySuggestionRequest {
    private String description;

    // Constructors
    public CategorySuggestionRequest() {}

    public CategorySuggestionRequest(String description) {
        this.description = description;
    }

    // Getters and Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.example.demo.DTOs;

public class CategorySuggestionResponse {
    private String suggestedCategory;
    private boolean isNewSuggestion; // Indicates if ChatGPT suggested a new category

    // Constructors
    public CategorySuggestionResponse() {}

    public CategorySuggestionResponse(String suggestedCategory, boolean isNewSuggestion) {
        this.suggestedCategory = suggestedCategory;
        this.isNewSuggestion = isNewSuggestion;
    }

    // Getters and Setters
    public String getSuggestedCategory() {
        return suggestedCategory;
    }

    public void setSuggestedCategory(String suggestedCategory) {
        this.suggestedCategory = suggestedCategory;
    }

    public boolean isNewSuggestion() {
        return isNewSuggestion;
    }

    public void setNewSuggestion(boolean newSuggestion) {
        isNewSuggestion = newSuggestion;
    }
}

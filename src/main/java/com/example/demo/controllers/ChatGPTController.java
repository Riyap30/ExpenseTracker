package com.example.demo.controllers;

import com.example.demo.DTOs.CategorySuggestionRequest;
import com.example.demo.DTOs.CategorySuggestionResponse;
import com.example.demo.entities.Category;
import com.example.demo.entities.Group;
import com.example.demo.entities.User;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.GroupRepository;
import com.example.demo.services.ChatGPTService;

import com.example.demo.services.GroupServices;
import com.example.demo.util.ApplicationContextHolder;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/chatgpt")
public class ChatGPTController {

    @Autowired
    private ChatGPTService chatGPTService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    GroupServices groupServices;

    @Autowired
    ApplicationContextHolder applicationContext;

    @PostMapping("/suggest-and-save")
    public ResponseEntity<CategorySuggestionResponse> suggestAndSaveCategory(@RequestBody CategorySuggestionRequest request) {
        // Step 1: Get suggested category from ChatGPT
        String suggestedCategory = chatGPTService.getSuggestedCategory(request.getDescription());

        // Step 2: Check if the category already exists
        Optional<Category> existingCategory = categoryRepository.findByName(suggestedCategory);

        if (existingCategory.isEmpty()) {
            // Step 3: Save new category if not already present
            Category newCategory = new Category();
            newCategory.setName(suggestedCategory);
            categoryRepository.save(newCategory);
        }

        // Step 4: Respond with the category name and status
        boolean isNewSuggestion = existingCategory.isEmpty(); // True if newly saved
        return ResponseEntity.ok(new CategorySuggestionResponse(suggestedCategory, isNewSuggestion));
    }
}

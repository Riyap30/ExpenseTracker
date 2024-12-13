package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.CategoryRepository;
import com.example.demo.DTOs.CategoryDTO;
import com.example.demo.entities.*;


@Service
public class CategoryServices {
    
    @Autowired
    private CategoryRepository categoryRepository;


    // find all categories
    public List<CategoryDTO> findAllCategories() {
        List<CategoryDTO> categories = categoryRepository.findAll().stream().map(
            category -> new CategoryDTO(category.getId(), category.getName())
        ).toList();

        return categories;
    }


    // find category by id
    public Optional<CategoryDTO> findCategoryById(Long id) {
        Optional<CategoryDTO> categoryFound = categoryRepository.findById(id).map(
            category -> new CategoryDTO(category.getId(), category.getName())
        );
        return categoryFound;
    }

    // find category by name
    public Optional<Category> findCategoryByName(String name) {
        Optional<Category> categoryFound = categoryRepository.findByName(name);
        return categoryFound;
    }

}

package com.example.demo.util;

import com.example.demo.entities.Category;
import com.example.demo.entities.Group;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.GroupRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CategoryInitializer {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GroupRepository groupRepository;

    @PostConstruct
    public void init() {
        Group globalGroup = groupRepository.findByName("ALL")
                .orElseGet(() -> {
                    Group newGlobalGroup = new Group();
                    newGlobalGroup.setName("ALL");
                    return groupRepository.save(newGlobalGroup);
                });

        List<String> globalCategories = List.of("Food", "Transportation", "Entertainment", "Housing", "Miscellaneous");

        globalCategories.forEach(categoryName -> {
            if (!categoryRepository.existsByName(categoryName)) {
                Category category = new Category();
                category.setName(categoryName);
                category.setGroup(globalGroup); // Associate with "ALL" group
                categoryRepository.save(category);
            }
        });
    }
}


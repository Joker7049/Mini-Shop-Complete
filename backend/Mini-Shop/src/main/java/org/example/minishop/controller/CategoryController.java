package org.example.minishop.controller;


import org.example.minishop.util.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @GetMapping
    public List<String> getAllCategories() {
        return Arrays.stream(Category.values()).map(Category::getDisplayName).toList();
    }
}

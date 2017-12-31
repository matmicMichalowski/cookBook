package com.matmic.cookbook.service;

import com.matmic.cookbook.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAll();
    CategoryDTO saveCategory(CategoryDTO categoryDTO);
    CategoryDTO findCategoryByName(String name);
    void deleteCategory(Long id);
}

package com.matmic.cookbook.service;

import com.matmic.cookbook.domain.Category;
import com.matmic.cookbook.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAll();
    CategoryDTO saveCategory(CategoryDTO categoryDTO);
    Category findByName(String name);
    CategoryDTO findCategoryByName(String name);
    void deleteCategory(Long id);
}

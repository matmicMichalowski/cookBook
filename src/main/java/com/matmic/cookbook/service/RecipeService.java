package com.matmic.cookbook.service;

import com.matmic.cookbook.domain.Category;
import com.matmic.cookbook.dto.RecipeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeService {
    Page<RecipeDTO> allRecipes(Pageable pageable);
    RecipeDTO findRecipeById(Long id);
    List<RecipeDTO> findRecipeByUser(String username);
    List<RecipeDTO> findRecipeByCategory(Category category);
    List<RecipeDTO> findRecipeByRatingValue(int low, int high);
    List<RecipeDTO> findRecipeByRatingAboveValue(int aboveValue);
    List<RecipeDTO> findRecipeByRatingBelowValue(int belowValue);
    RecipeDTO saveOrUpdateRecipe(RecipeDTO recipeDTO);
    void deleteRecipe(Long id);
}

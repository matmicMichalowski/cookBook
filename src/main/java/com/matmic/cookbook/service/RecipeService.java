package com.matmic.cookbook.service;

import com.matmic.cookbook.dto.RecipeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeService {
    Page<RecipeDTO> allRecipes(Pageable pageable);
    RecipeDTO findRecipeById(Long id);
    List<RecipeDTO> findRecipeByUser(String username);
    List<RecipeDTO> findRecipeByCategory(String categoryName);
    List<RecipeDTO> findRecipeByRatingValue(double low, double high);
    List<RecipeDTO> findRecipeByRatingAboveValue(double aboveValue);
    List<RecipeDTO> findRecipeByRatingBelowValue(double belowValue);
    RecipeDTO saveOrUpdateRecipe(RecipeDTO recipeDTO);
    void deleteRecipe(Long id);
}

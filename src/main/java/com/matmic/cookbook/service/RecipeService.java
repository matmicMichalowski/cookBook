package com.matmic.cookbook.service;

import com.matmic.cookbook.dto.RecipeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeService {
    Page<RecipeDTO> findAllRecipes(Pageable pageable);
    RecipeDTO findRecipeById(Long id);
    List<RecipeDTO> findRecipeByCategory(String categoryName);
    List<RecipeDTO> findRecipeByRatingValueBetweenLowAndHigh(int low, int high);
    List<RecipeDTO> findRecipeByRatingAboveValue(int aboveValue);
    List<RecipeDTO> findRecipeByRatingBelowValue(int belowValue);
    RecipeDTO createNewRecipe(RecipeDTO recipeDTO, Long userId);
    RecipeDTO saveOrUpdateRecipe(RecipeDTO recipeDTO);
    void deleteRecipe(Long id);
}

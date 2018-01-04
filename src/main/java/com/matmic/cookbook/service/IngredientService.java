package com.matmic.cookbook.service;

import com.matmic.cookbook.dto.IngredientDTO;

import java.util.Set;

public interface IngredientService {
    Set<IngredientDTO> getAllIngredientsFromRecipe(Long recipeId);
    IngredientDTO findByRecipeIdAndIngredientId(Long recipeId, Long id);
    IngredientDTO getIngredientFromRecipe(Long recipeId, Long ingredientId);
    IngredientDTO saveOrUpdateIngredient(IngredientDTO ingredientDTO, Long recipeId);
    void deleteIngredient(Long recipeId, Long id);
}

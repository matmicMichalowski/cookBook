package com.matmic.cookbook.service;

import com.matmic.cookbook.dto.IngredientDTO;

public interface IngredientService {
    IngredientDTO getIngredientFromRecipe(Long recipeId, Long ingredientId);
    IngredientDTO saveOrUpdateIngredient(IngredientDTO ingredientDTO);
    void deleteIngredient(Long recipeId, Long id);
}

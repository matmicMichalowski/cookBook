package com.matmic.cookbook.controller;

import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.service.IngredientService;
import com.matmic.cookbook.service.RecipeService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    public static final String ENTITY_NAME = "recipe";

    private final RecipeService recipeService;
    private final IngredientService ingredientService;


    public RecipeController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<RecipeDTO>> getAllRecipes(Pageable pageable){
        return null;
    }
}

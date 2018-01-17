package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.util.HttpHeadersUtil;
import com.matmic.cookbook.controller.util.PaginationUtil;
import com.matmic.cookbook.dto.IngredientDTO;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.service.IngredientService;
import com.matmic.cookbook.service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class RecipeController {

    public static final String ENTITY_NAME = "recipe";

    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public RecipeController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeDTO>> getAllRecipes(Pageable pageable){
        Page<RecipeDTO> page = recipeService.findAllRecipes(pageable);
        HttpHeaders headers = PaginationUtil.paginationHttpHeader(page, "/api/recipes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable Long id){
        return new ResponseEntity<>(recipeService.findRecipeById(id), HttpStatus.OK);
    }

    @GetMapping("/recipes/above/{aboveValue}")
        public ResponseEntity<List<RecipeDTO>> getRecipesAboveRating(@PathVariable int aboveValue){
            return new ResponseEntity<>(recipeService.findRecipeByRatingAboveValue(aboveValue), HttpStatus.OK);
        }

    @GetMapping("/recipes/above/{aboveValue}/below/{belowValue}")
    public ResponseEntity<List<RecipeDTO>> getRecipesInRatingRange(@PathVariable int aboveValue, @PathVariable int belowValue){
        return new ResponseEntity<>(recipeService.findRecipeByRatingValueBetweenLowAndHigh(aboveValue, belowValue), HttpStatus.OK);
    }

    @GetMapping("/recipes/below/{belowValue}")
    public ResponseEntity<List<RecipeDTO>> getRecipesBelowRating(@PathVariable int belowValue){
        return new ResponseEntity<>(recipeService.findRecipeByRatingBelowValue(belowValue), HttpStatus.OK);
    }


    @GetMapping("/recipes/{categoryName}")
    public ResponseEntity<List<RecipeDTO>> getRecipesByCategory(@PathVariable String categoryName){
        return new ResponseEntity<>(recipeService.findRecipeByCategory(categoryName), HttpStatus.OK);
    }

    @GetMapping("/recipe/{id}/ingredients")
    public ResponseEntity<Set<IngredientDTO>> getIngredientsFromRecipe(@PathVariable Long id){
        return new ResponseEntity<>(ingredientService.getAllIngredientsFromRecipe(id), HttpStatus.OK);
    }

    @GetMapping("/recipe/{id}/ingredient/{ingredientId}")
    public ResponseEntity<IngredientDTO> findIngredientInRecipe(@PathVariable Long id, @PathVariable Long ingredientId){
        return new ResponseEntity<>(ingredientService.findByRecipeIdAndIngredientId(id, ingredientId), HttpStatus.OK);
    }

    @PostMapping("user/{userId}/recipe")
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO recipeDTO, @PathVariable Long userId) throws URISyntaxException{
        if (recipeDTO.getId() != null){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "A new recipe cannot be created, it have already ID")).body(null);
        }
        RecipeDTO savedRecipe = recipeService.createNewRecipe(recipeDTO, userId);
        return ResponseEntity.created(new URI("/api/recipe/" + savedRecipe.getId().toString()))
                .headers(HttpHeadersUtil.createdEntityAlert(ENTITY_NAME, savedRecipe.getId().toString()))
                .body(savedRecipe);
    }

    @PutMapping("/user/{userId}/recipe")
    public ResponseEntity<RecipeDTO> updateRecipe(@RequestBody RecipeDTO recipeDTO, @PathVariable Long userId) throws URISyntaxException{
        if (recipeDTO.getId() == null){
            return createRecipe(recipeDTO, userId);
        }
        RecipeDTO updated = recipeService.saveOrUpdateRecipe(recipeDTO);
        return ResponseEntity.ok().headers(HttpHeadersUtil.updateEntityAlert(ENTITY_NAME, updated.getId().toString()))
                .body(updated);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id){
        recipeService.deleteRecipe(id);
        return ResponseEntity.ok().headers(HttpHeadersUtil.deleteEntityAlert(ENTITY_NAME, id.toString())).build();
    }


}

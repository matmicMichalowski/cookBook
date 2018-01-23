package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.util.HttpHeadersUtil;
import com.matmic.cookbook.controller.util.PaginationUtil;
import com.matmic.cookbook.dto.IngredientDTO;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.service.IngredientService;
import com.matmic.cookbook.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger log = LoggerFactory.getLogger(RecipeController.class);

    public static final String ENTITY_NAME = "recipe";

    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public RecipeController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    /**
     * GET /recipes : get all Recipes
     *
     * @param pageable pagination information
     * @return ResponseEntity with status 200 OK and body with list of recipeDTO
     */
    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeDTO>> getAllRecipes(Pageable pageable){
        log.debug("REST request to get all recipes");
        Page<RecipeDTO> page = recipeService.findAllRecipes(pageable);
        HttpHeaders headers = PaginationUtil.paginationHttpHeader(page, "/api/recipes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /recipe/:id : get Recipe by id
     *
     * @param id the id of recipe
     * @return ResponseEntity with status 200 OK and body with recipeDTO
     */
    @GetMapping("/recipe/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable Long id){
        log.debug("REST request to get recipe by id: {}", id);
        return new ResponseEntity<>(recipeService.findRecipeById(id), HttpStatus.OK);
    }

    /**
     * GET /recipe/above/:aboveValue : get all recipes with rating above given value
     *
     * @param aboveValue above boundary value
     * @return ResponseEntity with status 200 OK and body with recipeDTO list
     */
    @GetMapping("/recipes/above/{aboveValue}")
        public ResponseEntity<List<RecipeDTO>> getRecipesAboveRating(@PathVariable int aboveValue){
            log.debug("REST request to get recipes above rating value: {}", aboveValue);
            return new ResponseEntity<>(recipeService.findRecipeByRatingAboveValue(aboveValue), HttpStatus.OK);
        }

    /**
     * GET /recipe/above/:aboveValue : get all recipes between given rating values
     *
     * @param aboveValue above boundary value
     * @param belowValue below boundary value
     * @return ResponseEntity with status 200 OK and body with recipeDTO list
     */
    @GetMapping("/recipes/above/{aboveValue}/below/{belowValue}")
    public ResponseEntity<List<RecipeDTO>> getRecipesInRatingRange(@PathVariable int aboveValue, @PathVariable int belowValue){
        log.debug("REST request to get recipes between two value ratings: {}, {}", aboveValue, belowValue);
        return new ResponseEntity<>(recipeService.findRecipeByRatingValueBetweenLowAndHigh(aboveValue, belowValue), HttpStatus.OK);
    }


    /**
     * GET /recipe/above/:belowValue : get all recipes with rating below given value
     *
     * @param belowValue below boundary value
     * @return ResponseEntity with status 200 OK and body with recipeDTO list
     */
    @GetMapping("/recipes/below/{belowValue}")
    public ResponseEntity<List<RecipeDTO>> getRecipesBelowRating(@PathVariable int belowValue){
        log.debug("REST request to get recipes above rating value: {}", belowValue);
        return new ResponseEntity<>(recipeService.findRecipeByRatingBelowValue(belowValue), HttpStatus.OK);
    }

    /**
     * GET /recipe/:categoryName : get all recipes with given category
     *
     * @param categoryName above boundary value
     * @return ResponseEntity with status 200 OK and body with recipeDTO
     */
    @GetMapping("/recipes/{categoryName}")
    public ResponseEntity<List<RecipeDTO>> getRecipesByCategory(@PathVariable String categoryName){
        log.debug("REST request to get recipes by category name: {}", categoryName);
        return new ResponseEntity<>(recipeService.findRecipeByCategory(categoryName), HttpStatus.OK);
    }

    /**
     * GET /recipe/:id/ingredients : get all ingredients from recipe
     *
     * @param id recipe id
     * @return ResponseEntity with status 200 OK and set of ingredientDTO body
     */
    @GetMapping("/recipe/{id}/ingredients")
    public ResponseEntity<Set<IngredientDTO>> getIngredientsFromRecipe(@PathVariable Long id){
        log.debug("REST request to get ingredients from recipe: {}", id);
        return new ResponseEntity<>(ingredientService.getAllIngredientsFromRecipe(id), HttpStatus.OK);
    }

    /**
     * GET /recipe/:id/ingredient/:ingredientId : get one ingredient from recipe by id
     *
     * @param id the recipe id
     * @param ingredientId the ingredient id
     * @return ResponseEntity with status 200 OK with ingredientDTO in body
     */
    @GetMapping("/recipe/{id}/ingredient/{ingredientId}")
    public ResponseEntity<IngredientDTO> findIngredientInRecipe(@PathVariable Long id, @PathVariable Long ingredientId){
        log.debug("REST request to get single ingredient from recipe: {}, {}", id, ingredientId);
        return new ResponseEntity<>(ingredientService.findByRecipeIdAndIngredientId(id, ingredientId), HttpStatus.OK);
    }

    /**
     * POST /user/:userId/recipe : create new recipe
     *
     * @param recipeDTO recipeDTO to save
     * @param userId user id
     * @return the ResponseEntity with status 201 Created and with body of saved recipeDTO,
     * or with status 400 BadRequest if recipeDTO has already an ID parameter
     * @throws URISyntaxException if new Recipe Location URI syntax is incorrect
     */
    @PostMapping("user/{userId}/recipe")
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO recipeDTO, @PathVariable Long userId) throws URISyntaxException{
        log.debug("REST request to create recipe: {}", recipeDTO);
        if (recipeDTO.getId() != null){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "A new recipe cannot be created, it have already ID")).body(null);
        }
        RecipeDTO savedRecipe = recipeService.createNewRecipe(recipeDTO, userId);
        return ResponseEntity.created(new URI("/api/recipe/" + savedRecipe.getId().toString()))
                .headers(HttpHeadersUtil.createdEntityAlert(ENTITY_NAME, savedRecipe.getId().toString()))
                .body(savedRecipe);
    }

    /**
     * PUT /:id : update Recipe with given id
     *
     * @param recipeDTO recipeDTO to be udated
     * @param userId user id
     * @return the ResponseEntity with status 200 OK and with body the updated recipeDTO,
     * or with status 400 Bad Request if the recipeDTO is not valid,
     * @throws URISyntaxException if the Recipe Location URI syntax is incorrect
     */
    @PutMapping("/user/{userId}/recipe")
    public ResponseEntity<RecipeDTO> updateRecipe(@RequestBody RecipeDTO recipeDTO, @PathVariable Long userId) throws URISyntaxException{
        log.debug("REST request to update recipe: {}", recipeDTO);
        if (recipeDTO.getId() == null){
            return createRecipe(recipeDTO, userId);
        }
        RecipeDTO updated = recipeService.saveAndUpdateRecipe(recipeDTO);
        return ResponseEntity.ok().headers(HttpHeadersUtil.updateEntityAlert(ENTITY_NAME, updated.getId().toString()))
                .body(updated);
    }

    /**
     * DELETE /recipe/:id : delete Recipe by id
     *
     * @param id id of Recipe to delete
     * @return ResponseEntity with status 200 OK
     */
    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id){
        log.debug("REST request to delete recipe: {}", id);
        recipeService.deleteRecipe(id);
        return ResponseEntity.ok().headers(HttpHeadersUtil.deleteEntityAlert(ENTITY_NAME, id.toString())).build();
    }


}

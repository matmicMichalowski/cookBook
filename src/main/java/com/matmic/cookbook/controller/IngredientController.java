package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.util.HttpHeadersUtil;
import com.matmic.cookbook.dto.IngredientDTO;
import com.matmic.cookbook.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * REST controller for managing Ingredient
 */
@RestController
@RequestMapping("/api/ingredient")
public class IngredientController {

    public static final String ENTITY_NAME = "ingredient";

    private final IngredientService ingredientService;


    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * GET /:id/recipe/:recipeId : get ingredient by id from recipe by id
     *
     * @param recipeId the id of the recipe
     * @param id the id of the ingredient
     * @return ResponseEntity with status 200 OK and body ingredientDTO
     */
    @GetMapping("/{id}/recipe/{recipeId}")
    public ResponseEntity<IngredientDTO> getIngredient(@PathVariable Long recipeId, @PathVariable Long id){
        IngredientDTO ingredient = ingredientService.findByRecipeIdAndIngredientId(recipeId, id);
        return new ResponseEntity<>(ingredient, HttpStatus.OK);
    }

    /**
     * POST : create new Ingredient
     *
     * @param ingredientDTO ingredientDTO to be saved
     * @return the ResponseEntity with status 201 Created and with body the new ingredientDTO,
     * or with status 400 Bad Request if the ingredientDTO has already an ID
     * @throws URISyntaxException if the Ingredient Location URI syntax is incorrect
     */
    @PostMapping
    public ResponseEntity<IngredientDTO> createIngredient(@RequestBody IngredientDTO ingredientDTO) throws URISyntaxException{
        if (ingredientDTO.getId() != null){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME,
                    "A new ingredient can not be created. Ingredient ID is present.")).body(null);
        }
        IngredientDTO savedIngredient = ingredientService.saveOrUpdateIngredient(ingredientDTO);
        return ResponseEntity.created(new URI("/api/ingredient/" + savedIngredient.getId()))
                .headers(HttpHeadersUtil.createdEntityAlert(ENTITY_NAME, savedIngredient.getId().toString()))
                .body(savedIngredient);
    }

    /**
     * PUT : update Ingredient
     *
     * @param ingredientDTO ingredientDTO to update
     * @return the ResponseEntity with status 200 OK and with body the updated ingredientDTO,
     * or with status 400 Bad Request if the ingredientDTO is not valid,
     * @throws URISyntaxException if the Ingredient Location URI syntax is incorrect
     */
    @PutMapping
    public ResponseEntity<IngredientDTO> updateIngredient(@RequestBody IngredientDTO ingredientDTO) throws URISyntaxException{
        if (ingredientDTO.getId() == null){
            return createIngredient(ingredientDTO);
        }
        IngredientDTO updatedIngredient = ingredientService.saveOrUpdateIngredient(ingredientDTO);
        return ResponseEntity.created(new URI("/api/ingredient/" + updatedIngredient.getId()))
                .headers(HttpHeadersUtil.updateEntityAlert(ENTITY_NAME, updatedIngredient.getId().toString()))
                .body(updatedIngredient);
    }

    /**
     * DELETE /:id/recipe/:recipeId : delete Ingredient with given id from Recipe with given id
     *
     * @param id the id of the Ingredient to delete
     * @param recipeId the id of Recipe that store Ingredient
     * @return ResponseEntity with status 200 OK
     */
    @DeleteMapping("/{id}/recipe/{recipeId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id, @PathVariable Long recipeId){
        ingredientService.deleteIngredient(recipeId, id);
        return ResponseEntity.ok().headers(HttpHeadersUtil.deleteEntityAlert(ENTITY_NAME, id.toString())).build();
    }
}

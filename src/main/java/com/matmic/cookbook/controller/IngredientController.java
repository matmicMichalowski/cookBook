package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.util.HttpHeadersUtil;
import com.matmic.cookbook.dto.IngredientDTO;
import com.matmic.cookbook.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;


@RestController
@RequestMapping("/api/ingredient")
public class IngredientController {

    public static final String ENTITY_NAME = "ingredient";

    private final IngredientService ingredientService;


    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/{id}/{recipeId}")
    public ResponseEntity<IngredientDTO> getIngredient(@PathVariable Long recipeId, @PathVariable Long id){
        return new ResponseEntity<>(ingredientService.findByRecipeIdAndIngredientId(recipeId, id), HttpStatus.OK);
    }


    @PostMapping("/recipe/{recipeId}")
    public ResponseEntity<IngredientDTO> createIngredient(@PathVariable Long recipeId, @RequestBody IngredientDTO ingredientDTO) throws URISyntaxException{
        if (ingredientDTO.getId() != null){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME,
                    "A new ingredient can not be created. Ingredient ID is present.")).body(null);
        }
        IngredientDTO savedIngredient = ingredientService.saveOrUpdateIngredient(ingredientDTO, recipeId);
        return ResponseEntity.created(new URI("/api/ingredient/" + savedIngredient.getId()))
                .headers(HttpHeadersUtil.createdEntityAlert(ENTITY_NAME, savedIngredient.getId().toString()))
                .body(savedIngredient);
    }

    @PutMapping("/recipe/{recipeId}")
    public ResponseEntity<IngredientDTO> updateIngredient(@PathVariable Long recipeId, @RequestBody IngredientDTO ingredientDTO) throws URISyntaxException{
        if (ingredientDTO.getId() == null){
            return createIngredient(recipeId, ingredientDTO);
        }
        IngredientDTO updatedIngredient = ingredientService.saveOrUpdateIngredient(ingredientDTO, recipeId);
        return ResponseEntity.created(new URI("/api/ingredient/" + updatedIngredient.getId()))
                .headers(HttpHeadersUtil.updateEntityAlert(ENTITY_NAME, updatedIngredient.getId().toString()))
                .body(updatedIngredient);
    }

    @DeleteMapping("/{id}/recipe/{recipeId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id, @PathVariable Long recipeId){
        ingredientService.deleteIngredient(recipeId, id);
        return ResponseEntity.ok().headers(HttpHeadersUtil.deleteEntityAlert(ENTITY_NAME, id.toString())).build();
    }
}

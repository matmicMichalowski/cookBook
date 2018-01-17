package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.IngredientDtoToIngredient;
import com.matmic.cookbook.converter.IngredientToIngredientDto;
import com.matmic.cookbook.domain.Ingredient;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.dto.IngredientDTO;
import com.matmic.cookbook.repository.RecipeRepository;
import com.matmic.cookbook.repository.UnitOfMeasureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Ingredient
 */
@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private final Logger log = LoggerFactory.getLogger(IngredientServiceImpl.class);

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientDtoToIngredient toIngredient;
    private final IngredientToIngredientDto toIngredientDto;

    public IngredientServiceImpl(RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository, IngredientDtoToIngredient toIngredient, IngredientToIngredientDto toIngredientDto) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.toIngredient = toIngredient;
        this.toIngredientDto = toIngredientDto;
    }

    /**
     * Get all ingredients from Recipe.
     *
     * @param recipeId id of the Recipe
     * @return set of Recipe Ingredients entities
     */
    @Override
    @Transactional(readOnly = true)
    public Set<IngredientDTO> getAllIngredientsFromRecipe(Long recipeId){
        log.debug("Request to get all ingredients from Recipe: {}", recipeId);
        Optional<Recipe> recipeOptional =  recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()){
            Recipe recipeFound = recipeOptional.get();
            return recipeFound.getIngredients().stream()
                    .map(toIngredientDto::convert)
                    .collect(Collectors.toSet());
        }
        return null;
    }

    /**
     * Get one Ingredient entity from given Recipe
     *
     * @param recipeId id of the Recipe entity
     * @param id id of the Ingredient entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public IngredientDTO findByRecipeIdAndIngredientId(Long recipeId, Long id){
        log.debug("Request to get one Ingredient with id: {}, from Recipe: {}", id, recipeId);
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()){
            Recipe recipeFound = recipeOptional.get();
            return recipeFound.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(id))
                    .findFirst()
                    .map(toIngredientDto::convert)
                    .orElseThrow(RuntimeException::new);
        }
        return null;
    }

    /**
     * Save and upgrade Ingredient
     *
     * @param ingredientDTO id of entity
     * @return saved entity
     */
    @Override
    public IngredientDTO saveOrUpdateIngredient(IngredientDTO ingredientDTO) {
        log.debug("Request to save Inrgedient entity: {}", ingredientDTO);
        Optional<Recipe> optional = recipeRepository.findById(ingredientDTO.getRecipeId());

        if(!optional.isPresent()){
            return new IngredientDTO();
        }else{
            Recipe recipe = optional.get();

            Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientDTO.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()){
                Ingredient toUpdate = ingredientOptional.get();
                toUpdate.setAmount(ingredientDTO.getAmount());
                toUpdate.setName(ingredientDTO.getName());
                toUpdate.setUnitOfMeasure(unitOfMeasureRepository.findById(ingredientDTO.getUnitOfMeasure().getId())
                .orElseThrow(()-> new RuntimeException("Unit not found")));
            }else{
                Ingredient newIngredient = toIngredient.convert(ingredientDTO);
                newIngredient.setRecipe(recipe);
                recipe.getIngredients().add(newIngredient);
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredient = savedRecipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientDTO.getId())).findFirst();

            if (!savedIngredient.isPresent()){
                savedIngredient = savedRecipe.getIngredients().stream()
                        .filter(ingredient -> ingredient.getName().equals(ingredientDTO.getName()))
                        .filter(ingredient -> ingredient.getAmount() == ingredientDTO.getAmount())
                        .filter(ingredient -> ingredient.getUnitOfMeasure().getId()
                                .equals(ingredientDTO.getUnitOfMeasure().getId()))
                        .findFirst();
            }

            return toIngredientDto.convert(savedIngredient.get());
        }
    }

    /**
     * Delete Ingredient from Recipe by id and recipe id
     *
     * @param recipeId id of recipe
     * @param id the id of the ingredient entity
     */
    @Override
    public void deleteIngredient(Long recipeId, Long id) {
        log.debug("Request to delete Ingredient with id: {}, from Recipe: {}", id, recipeId);
        Optional<Recipe> optional = recipeRepository.findById(recipeId);

        if (optional.isPresent()){
            Recipe recipe = optional.get();

            Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(id))
                    .findFirst();
            if (ingredientOptional.isPresent()){
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setRecipe(null);
                recipe.getIngredients().remove(ingredientFound);
                recipeRepository.save(recipe);
            }
        }
    }
}

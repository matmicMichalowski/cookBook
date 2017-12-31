package com.matmic.cookbook.service;

import com.matmic.cookbook.domain.Ingredient;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.dto.IngredientDTO;
import com.matmic.cookbook.mapper.IngredientMapper;
import com.matmic.cookbook.repository.RecipeRepository;
import com.matmic.cookbook.repository.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientServiceImpl(RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository, IngredientMapper ingredientMapper) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientMapper = ingredientMapper;
    }

    @Override
    public IngredientDTO getIngredientFromRecipe(Long recipeId, Long ingredientId) {
        Optional<Recipe> optional = recipeRepository.findById(recipeId);
        if(!optional.isPresent()){
            return null;
        }

        Recipe recipe = optional.get();

        Optional<IngredientDTO> filterIngredient = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredient -> ingredientMapper.ingredientToIngredientDto(ingredient)).findFirst();

        if (!filterIngredient.isPresent()){
            return null;
        }

        return filterIngredient.get();
    }

    @Override
    public IngredientDTO saveOrUpdateIngredient(IngredientDTO ingredientDTO) {
        Optional<Recipe> optional = recipeRepository.findById(ingredientDTO.getRecipe().getId());

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
                Ingredient newIngredient = ingredientMapper.ingredientDtoToIngredient(ingredientDTO);
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

            return ingredientMapper.ingredientToIngredientDto(savedIngredient.get());
        }
    }


    @Override
    public void deleteIngredient(Long recipeId, Long id) {

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

package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.IngredientDtoToIngredient;
import com.matmic.cookbook.converter.IngredientToIngredientDto;
import com.matmic.cookbook.domain.Ingredient;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.dto.IngredientDTO;
import com.matmic.cookbook.repository.RecipeRepository;
import com.matmic.cookbook.repository.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

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

    @Override
    public Set<IngredientDTO> getAllIngredientsFromRecipe(Long recipeId){
        Optional<Recipe> recipeOptional =  recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()){
            Recipe recipeFound = recipeOptional.get();
            return recipeFound.getIngredients().stream()
                    .map(toIngredientDto::convert)
                    .collect(Collectors.toSet());
        }
        return null;
    }

    @Override
    public IngredientDTO findByRecipeIdAndIngredientId(Long recipeId, Long id){
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

    @Override
    public IngredientDTO saveOrUpdateIngredient(IngredientDTO ingredientDTO) {
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

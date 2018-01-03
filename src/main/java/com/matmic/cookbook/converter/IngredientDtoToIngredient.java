package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Ingredient;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.dto.IngredientDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientDtoToIngredient implements Converter<IngredientDTO, Ingredient>{

    private final UnitOfMeasureDtoToUnitOfMeasure uomConverter;

    public IngredientDtoToIngredient(UnitOfMeasureDtoToUnitOfMeasure uomConverter) {
        this.uomConverter = uomConverter;
    }

    @Synchronized
        @Nullable
        @Override
        public Ingredient convert(IngredientDTO ingredientDTO) {
            if(ingredientDTO == null) {
                return null;
            }

            final Ingredient ingredient = new Ingredient();
            ingredient.setId(ingredientDTO.getId());
            ingredient.setAmount(ingredientDTO.getAmount());
            ingredient.setName(ingredientDTO.getName());
            ingredient.setUnitOfMeasure(uomConverter.convert(ingredientDTO.getUnitOfMeasure()));
            if (ingredientDTO.getRecipeId() != null){
                Recipe recipe = new Recipe();
                recipe.setId(ingredientDTO.getRecipeId());
                ingredient.setRecipe(recipe);
                recipe.getIngredients().add(ingredient);
            }

            return ingredient;
        }

}

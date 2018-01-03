package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Ingredient;
import com.matmic.cookbook.dto.IngredientDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientToIngredientDto implements Converter<Ingredient, IngredientDTO>{

    private final UnitOfMeasureToUnitOfMeasureDto uomConverter;

    public IngredientToIngredientDto(UnitOfMeasureToUnitOfMeasureDto uomConverter) {
        this.uomConverter = uomConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public IngredientDTO convert(Ingredient ingredient) {
        if(ingredient == null) {
            return null;
        }

        final IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(ingredient.getId());
        ingredientDTO.setAmount(ingredient.getAmount());
        ingredientDTO.setName(ingredient.getName());
        ingredientDTO.setUnitOfMeasure(uomConverter.convert(ingredient.getUnitOfMeasure()));
        ingredientDTO.setRecipeId(ingredient.getRecipe().getId());

        return ingredientDTO;
    }
}

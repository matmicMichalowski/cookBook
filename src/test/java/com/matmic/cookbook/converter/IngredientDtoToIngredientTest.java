package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Ingredient;
import com.matmic.cookbook.dto.IngredientDTO;
import com.matmic.cookbook.dto.UnitOfMeasureDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IngredientDtoToIngredientTest {
    public static final Long ID_VALUE = 1L;
    public static final int AMOUNT = 3;
    public static final String NAME = "Ingredient";
    public static final Long RECIPE_ID = 4L;
    public static final Long UOM_ID = 2L;


    private IngredientDtoToIngredient converter;

    @Before
    public void setUp() throws Exception {
        converter = new IngredientDtoToIngredient(new UnitOfMeasureDtoToUnitOfMeasure());
    }

    @Test
    public void convert() throws Exception {
        UnitOfMeasureDTO unitOfMeasureDTO = new UnitOfMeasureDTO();
        unitOfMeasureDTO.setId(UOM_ID);

        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(ID_VALUE);
        ingredientDTO.setName(NAME);
        ingredientDTO.setUnitOfMeasure(unitOfMeasureDTO);
        ingredientDTO.setAmount(AMOUNT);
        ingredientDTO.setRecipeId(RECIPE_ID);

        Ingredient ingredient = converter.convert(ingredientDTO);

        assertNotNull(ingredient);
        assertEquals(ingredientDTO.getId(), ingredient.getId());
        assertEquals(ingredientDTO.getUnitOfMeasure().getId(), ingredient.getUnitOfMeasure().getId());
        assertEquals(ingredientDTO.getRecipeId(), ingredient.getRecipe().getId());

    }


}
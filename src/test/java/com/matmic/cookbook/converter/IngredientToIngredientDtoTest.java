package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Ingredient;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.UnitOfMeasure;
import com.matmic.cookbook.dto.IngredientDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IngredientToIngredientDtoTest {

    public static final Long ID_VALUE = 1L;
    public static final int AMOUNT = 3;
    public static final String NAME = "Ingredient";
    public static final Long RECIPE_ID = 4L;
    public static final Long UOM_ID = 2L;

    private IngredientToIngredientDto converter;

    @Before
    public void setUp() throws Exception {

        converter = new IngredientToIngredientDto(new UnitOfMeasureToUnitOfMeasureDto());
    }

    @Test
    public void convert() throws Exception {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(UOM_ID);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ID_VALUE);
        ingredient.setAmount(AMOUNT);
        ingredient.setName(NAME);
        ingredient.setRecipe(new Recipe());
        ingredient.getRecipe().setId(RECIPE_ID);
        ingredient.setUnitOfMeasure(unitOfMeasure);


        IngredientDTO ingredientDTO = converter.convert(ingredient);

        assertNotNull(ingredientDTO);
        assertEquals(ingredient.getId(), ingredientDTO.getId());
        assertEquals(ingredient.getRecipe().getId(), ingredientDTO.getRecipeId());
        assertEquals(ingredient.getName(), ingredientDTO.getName());
        assertEquals(ingredient.getAmount(), ingredientDTO.getAmount());
        assertEquals(ingredient.getUnitOfMeasure().getId(), ingredientDTO.getUnitOfMeasure().getId());

    }


}
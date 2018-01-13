package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.IngredientDtoToIngredient;
import com.matmic.cookbook.converter.IngredientToIngredientDto;
import com.matmic.cookbook.converter.UnitOfMeasureDtoToUnitOfMeasure;
import com.matmic.cookbook.converter.UnitOfMeasureToUnitOfMeasureDto;
import com.matmic.cookbook.domain.Ingredient;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.UnitOfMeasure;
import com.matmic.cookbook.dto.IngredientDTO;
import com.matmic.cookbook.dto.UnitOfMeasureDTO;
import com.matmic.cookbook.repository.RecipeRepository;
import com.matmic.cookbook.repository.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UnitOfMeasureRepository uomRepository;


    private IngredientDtoToIngredient toIngredient = new IngredientDtoToIngredient(new UnitOfMeasureDtoToUnitOfMeasure());


    private IngredientToIngredientDto toIngredientDto = new IngredientToIngredientDto(new UnitOfMeasureToUnitOfMeasureDto());

    private IngredientService ingredientService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ingredientService = new IngredientServiceImpl(recipeRepository, uomRepository, toIngredient, toIngredientDto);
    }

    @Test
    public void getAllIngredientsFromRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ing1 = new Ingredient();
        ing1.setId(2L);
        ing1.setRecipe(recipe);
        Ingredient ing2 = new Ingredient();
        ing2.setId(3L);
        ing2.setRecipe(recipe);
        recipe.getIngredients().add(ing1);
        recipe.getIngredients().add(ing2);

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        Set<IngredientDTO> ingredientDTOSet = ingredientService.getAllIngredientsFromRecipe(1L);

        assertNotNull(ingredientDTOSet);
        assertEquals(2, ingredientDTOSet.size());
        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    public void findByRecipeIdAndIngredientId() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(2L);
        ingredient.setName("Tomato");
        recipe.getIngredients().add(ingredient);
        ingredient.setRecipe(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        IngredientDTO ingredientFound = ingredientService.findByRecipeIdAndIngredientId(1L, 2L);

        assertNotNull(ingredientFound);
        assertEquals(ingredient.getName(), ingredientFound.getName());
        assertEquals(ingredient.getRecipe().getId(), ingredientFound.getRecipeId());
    }



    @Test
    public void saveOrUpdateIngredient() throws Exception {
        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(2L);
        ingredientDTO.setUnitOfMeasure(new UnitOfMeasureDTO());
        ingredientDTO.getUnitOfMeasure().setId(1L);

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(1L);
        unitOfMeasure.setName("Spoon");

        Ingredient ingredient = new Ingredient();
        ingredient.setId(2L);
        ingredient.setUnitOfMeasure(unitOfMeasure);

        Recipe recipe = new Recipe();
        recipe.setId(3L);

        recipe.getIngredients().add(ingredient);
        ingredient.setRecipe(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        when(uomRepository.findById(anyLong())).thenReturn(Optional.of(unitOfMeasure));
        when(recipeRepository.save(any())).thenReturn(recipe);

        IngredientDTO ingredientSaved = ingredientService.saveOrUpdateIngredient(ingredientDTO);

        assertNotNull(ingredientSaved);

    }

    @Test
    public void deleteIngredient() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(2L);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setRecipe(recipe);
        recipe.getIngredients().add(ingredient);

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        ingredientService.deleteIngredient(2L, 1L);

        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, times(1)).save(recipe);
    }

}
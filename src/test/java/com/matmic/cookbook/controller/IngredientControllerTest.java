package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.errors.RestResponseEntityExceptionHandler;
import com.matmic.cookbook.domain.Ingredient;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.UnitOfMeasure;
import com.matmic.cookbook.dto.IngredientDTO;
import com.matmic.cookbook.dto.UnitOfMeasureDTO;
import com.matmic.cookbook.repository.RecipeRepository;
import com.matmic.cookbook.service.IngredientService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.matmic.cookbook.controller.TestUtil.asJsonBytes;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {


    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientService ingredientService;

    private MockMvc mockMvc;

    private Ingredient ingredient;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        final IngredientController controller = new IngredientController(ingredientService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setRecipe(new Recipe());
        ingredient.getRecipe().setId(2L);
        ingredient.setName("Honey");
        ingredient.setAmount(20);
        ingredient.setUnitOfMeasure(new UnitOfMeasure());
        ingredient.getUnitOfMeasure().setId(1L);
    }

    @Test
    public void getIngredient() throws Exception {
        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(ingredient.getId());
        ingredientDTO.setName(ingredient.getName());
        ingredientDTO.setAmount(ingredient.getAmount());

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientDTO);

        mockMvc.perform(get("/api/ingredient/1/recipe/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name").value(ingredient.getName()))
                .andExpect(jsonPath("$.amount").value(ingredient.getAmount()));
    }

    @Test
    public void createIngredient() throws Exception {
        Recipe recipe = ingredient.getRecipe();

        IngredientDTO  ingredientDTO = new IngredientDTO();
        ingredientDTO.setUnitOfMeasure(new UnitOfMeasureDTO());
        ingredientDTO.setRecipeId(recipe.getId());
        ingredientDTO.setAmount(ingredient.getAmount());
        ingredientDTO.setName(ingredient.getName());

        IngredientDTO savedIngredient = new IngredientDTO();
        savedIngredient.setId(ingredient.getId());
        savedIngredient.setAmount(ingredient.getAmount());
        savedIngredient.setName(ingredient.getName());

        when(ingredientService.saveOrUpdateIngredient(any())).thenReturn(savedIngredient);

        mockMvc.perform(post("/api/ingredient")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(asJsonBytes(ingredientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(ingredientDTO.getAmount()))
                .andExpect(jsonPath("$.name").value(ingredientDTO.getName()));
    }

    @Test
    public void updateIngredient() throws Exception {
        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(1L);

        when(ingredientService.saveOrUpdateIngredient(any())).thenReturn(ingredientDTO);

        mockMvc.perform(put("/api/ingredient")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(asJsonBytes(ingredientDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteIngredient() throws Exception {

        mockMvc.perform(delete("/api/ingredient/2/recipe/2")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());

        verify(ingredientService, times(1)).deleteIngredient(anyLong(), anyLong());
    }

}
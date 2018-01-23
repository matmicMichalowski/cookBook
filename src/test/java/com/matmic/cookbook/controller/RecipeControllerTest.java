package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.errors.RestResponseEntityExceptionHandler;
import com.matmic.cookbook.converter.*;
import com.matmic.cookbook.domain.Difficulty;
import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.service.IngredientService;
import com.matmic.cookbook.service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.matmic.cookbook.controller.TestUtil.asJsonBytes;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @Mock
    private IngredientService ingredientService;


    private RecipeToRecipeDto toRecipeDto;

    private MockMvc mockMvc;

    private Recipe recipe;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        toRecipeDto = new RecipeToRecipeDto(new RatingToRatingDto(new EvaluationToEvaluationDto()), new CategoryToCategoryDto(),
                new IngredientToIngredientDto(new UnitOfMeasureToUnitOfMeasureDto()), new CommentToCommentDto());

        final RecipeController controller = new RecipeController(recipeService, ingredientService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        recipe = new Recipe();
        recipe.setId(1L);
        recipe.setUser(new User());
        recipe.getUser().setId(2L);
        recipe.getUser().getRecipes().add(recipe);
        recipe.setDifficulty(Difficulty.MODERATE);
        recipe.setUserName("Tester");
        recipe.setServings(2);
        recipe.setName("TestyPizza");
        recipe.setDirections("Test directions");
        recipe.setRating(new Rating());
        recipe.getRating().setId(1L);
        recipe.getRating().setRecipe(recipe);

    }

    @Test
    public void getAllRecipes() throws Exception {
        List<RecipeDTO> recipes = new ArrayList<>();
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(recipe.getId());
        recipeDTO.setName(recipe.getName());

        RecipeDTO recipeDTO2 = new RecipeDTO();
        recipeDTO2.setId(2L);
        recipeDTO2.setName(recipe.getName());
        recipes.add(recipeDTO);
        recipes.add(recipeDTO2);


        Page<RecipeDTO> recipeDTOPage = new PageImpl<>(recipes);

        when(recipeService.findAllRecipes(any())).thenReturn(recipeDTOPage);

        mockMvc.perform(get("/api/recipes?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].name").value(hasItem(recipe.getName())));
    }



    @Test
    public void createRecipe() throws Exception {

        RecipeDTO recipeDTO = toRecipeDto.convert(recipe);

        RecipeDTO toSave = toRecipeDto.convert(recipe);
        toSave.setId(null);

        when(recipeService.createNewRecipe(any(), anyLong())).thenReturn(recipeDTO);

        mockMvc.perform(post("/api/user/1/recipe")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(asJsonBytes(toSave)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(recipe.getName()))
                .andExpect(jsonPath("$.directions").value(recipe.getDirections()));
    }

    @Test
    public void updateRecipe() throws Exception {

        RecipeDTO recipeToUpdate = toRecipeDto.convert(recipe);

        when(recipeService.saveAndUpdateRecipe(any())).thenReturn(recipeToUpdate);

        mockMvc.perform(put("/api/user/1/recipe")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(asJsonBytes(recipeToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipe.getName()))
                .andExpect(jsonPath("$.directions").value(recipe.getDirections()));
    }

    @Test
    public void deleteRecipe() throws Exception {

        mockMvc.perform(delete("/api/recipe/3")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());

        verify(recipeService, times(1)).deleteRecipe(anyLong());
    }

}
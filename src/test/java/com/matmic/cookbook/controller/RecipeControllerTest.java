package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.errors.RestResponseEntityExceptionHandler;
import com.matmic.cookbook.converter.RecipeToRecipeDto;
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
import org.springframework.beans.factory.annotation.Autowired;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @Mock
    private IngredientService ingredientService;

    @Autowired
    private RecipeToRecipeDto toRecipeDto;

    private MockMvc mockMvc;

    private Recipe recipe;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        final RecipeController controller = new RecipeController(recipeService, ingredientService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        recipe = new Recipe();
        recipe.setId(1L);
        recipe.setUser(new User());
        recipe.getUser().setId(2L);
        recipe.setName("TestyPizza");
        recipe.setDirections("Test directions");
        recipe.setRating(new Rating());
        recipe.getRating().setId(1L);
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

        when(recipeService.createNewRecipe(any(), anyLong())).thenReturn(recipeDTO);

        mockMvc.perform(post("/api/recipe")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(asJsonBytes(recipeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(recipe.getName()))
                .andExpect(jsonPath("$.directions").value(recipe.getDirections()));
    }

    @Test
    public void updateRecipe() throws Exception {
    }

    @Test
    public void deleteRecipe() throws Exception {
    }

}
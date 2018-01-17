package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.*;
import com.matmic.cookbook.domain.Category;
import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.RatingDTO;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.repository.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    private RecipeToRecipeDto toRecipeDto;

    private RecipeDtoToRecipe toRecipe;

    @Mock
    private CategoryService categoryService;

    @Mock
    private UnitOfMeasureDtoToUnitOfMeasure toUnitOfMeasure;

    @Mock
    private UserService userService;

    private RecipeService recipeService;

    private Recipe recipe;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        toRecipe = new RecipeDtoToRecipe(new RatingDtoToRating(new EvaluationDtoToEvaluation()), new CategoryDtoToCategory(),
                new IngredientDtoToIngredient(new UnitOfMeasureDtoToUnitOfMeasure()), new CommentDtoToComment());

        toRecipeDto = new RecipeToRecipeDto(new RatingToRatingDto(new EvaluationToEvaluationDto()), new CategoryToCategoryDto(),
                new IngredientToIngredientDto(new UnitOfMeasureToUnitOfMeasureDto()), new CommentToCommentDto());

        recipeService = new RecipeServiceImpl(recipeRepository, toRecipeDto, toRecipe
                , categoryService, userService, toUnitOfMeasure);

        recipe = new Recipe();
        recipe.setId(1L);
        recipe.setUser(new User());
        recipe.getUser().setId(1L);
        recipe.getUser().getRecipes().add(recipe);
        recipe.setRating(new Rating());
        recipe.getRating().setId(1L);
        recipe.getRating().setRecipe(recipe);
    }

    @Test
    public void findAllRecipes() throws Exception {

        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipe);
        Page<Recipe> recipePage = new PageImpl<>(recipeList);

        when(recipeRepository.findAll(any(Pageable.class))).thenReturn(recipePage);

        Page<RecipeDTO> recipePageFound = recipeService.findAllRecipes(PageRequest.of(1, 20));

        assertNotNull(recipePageFound);
        assertEquals(1, recipePageFound.getTotalElements());
    }


    @Test
    public void findRecipeByCategory() throws Exception {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("european");
        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("american");

        Recipe recipe1 = new Recipe();
        recipe1.setId(5L);
        recipe1.setUser(new User());
        recipe1.getUser().setId(1L);
        recipe1.getUser().getRecipes().add(recipe1);
        recipe1.setRating(new Rating());
        recipe1.getRating().setId(2L);
        recipe1.getRating().setRecipe(recipe1);

        recipe1.getCategories().add(cat1);

        Recipe recipe2 = new Recipe();
        recipe1.setId(6L);
        recipe1.setUser(new User());
        recipe1.getUser().setId(1L);
        recipe1.getUser().getRecipes().add(recipe1);
        recipe1.setRating(new Rating());
        recipe1.getRating().setId(2L);
        recipe1.getRating().setRecipe(recipe1);

        recipe2.getCategories().add(cat2);

        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipe1);


        when(recipeRepository.findAllByCategoryName(anyString())).thenReturn(recipeList);

        List<RecipeDTO> recipeFound = recipeService.findRecipeByCategory("european");

        assertNotNull(recipeFound);
        assertEquals(1, recipeFound.size());
    }

    @Test
    public void findRecipeByRatingValue() throws Exception {
        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        recipe1.setUser(new User());


        Rating rating1 = new Rating();
        rating1.setTotalRating(4);
        rating1.setRecipe(recipe1);
        recipe1.setRating(rating1);

        List<Recipe> allRecipes = new ArrayList<>();
        allRecipes.add(recipe1);


        when(recipeRepository.findAllRecipesBetweenRatingValues(anyInt(), anyInt())).thenReturn(allRecipes);

        List<RecipeDTO> byRatingVal = recipeService.findRecipeByRatingValueBetweenLowAndHigh(1, 4);

        assertNotNull(byRatingVal);
        assertEquals(1, byRatingVal.size());
        assertEquals(1, allRecipes.size());
        verify(recipeRepository, times(1)).findAllRecipesBetweenRatingValues(anyInt(), anyInt());
    }

    @Test
    public void findRecipeByRatingAboveValue() throws Exception {
        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        recipe1.setUser(new User());

        Rating rating1 = new Rating();
        rating1.setTotalRating(4);
        rating1.setRecipe(recipe1);
        recipe1.setRating(rating1);

        List<Recipe> allRecipes = new ArrayList<>();
        allRecipes.add(recipe1);

        when(recipeRepository.findAllRecipesAboveRating(anyInt())).thenReturn(allRecipes);

        List<RecipeDTO> byRatingVal = recipeService.findRecipeByRatingAboveValue(1);

        assertNotNull(byRatingVal);
        assertEquals(1, byRatingVal.size());
        assertEquals(1, allRecipes.size());
        verify(recipeRepository, times(1)).findAllRecipesAboveRating(anyInt());
    }

    @Test
    public void findRecipeByRatingBelowValue() throws Exception {
        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        recipe1.setUser(new User());

        Rating rating1 = new Rating();
        rating1.setTotalRating(4);
        rating1.setRecipe(recipe1);
        recipe1.setRating(rating1);

        List<Recipe> allRecipes = new ArrayList<>();
        allRecipes.add(recipe1);

        when(recipeRepository.findAllRecipesBelowRating(anyInt())).thenReturn(allRecipes);

        List<RecipeDTO> byRatingVal = recipeService.findRecipeByRatingBelowValue(5);

        assertNotNull(byRatingVal);
        assertEquals(1, byRatingVal.size());
        assertEquals(1, allRecipes.size());
        verify(recipeRepository, times(1)).findAllRecipesBelowRating(anyInt());
    }

    @Test
    public void saveOrUpdateRecipe() throws Exception {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(2L);
        recipeDTO.setName("Burger");
        recipeDTO.setUserId(1L);
        recipeDTO.setRating(new RatingDTO());
        recipeDTO.getRating().setId(1L);


        when(recipeRepository.save(any())).thenReturn(recipe);

        RecipeDTO recipeSaved = recipeService.saveAndUpdateRecipe(recipeDTO);

        assertNotNull(recipeSaved);
        verify(recipeRepository, times(1)).save(any());

    }

    @Test
    public void createNewRecipe() throws Exception {
        User user = new User();
        user.setId(3L);

        Recipe recipe = new Recipe();
        recipe.setId(2L);
        recipe.setUser(user);

        RecipeDTO recipeDto = new RecipeDTO();
        recipeDto.setId(2L);

        when(userService.findUserByID(anyLong())).thenReturn(user);
        when(recipeRepository.save(any())).thenReturn(recipe);

        RecipeDTO createdRecipe = recipeService.createNewRecipe(recipeDto, 3L);

        assertNotNull(createdRecipe);
        assertEquals(user.getId(), recipe.getUser().getId());

    }

    @Test
    public void deleteRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setUser(new User());

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        recipeService.deleteRecipe(1L);

        verify(recipeRepository, times(1)).delete(any());
        verify(recipeRepository, never()).deleteAll();
    }

}
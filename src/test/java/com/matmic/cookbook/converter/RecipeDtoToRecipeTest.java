package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Difficulty;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.dto.CategoryDTO;
import com.matmic.cookbook.dto.CommentDTO;
import com.matmic.cookbook.dto.RatingDTO;
import com.matmic.cookbook.dto.RecipeDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecipeDtoToRecipeTest {
    public static final Long ID_VALUE = 1L;
    public static final String NAME = "Recipe name";
    public static final int COOK_TIME = 23;
    public static final int SERVINGS = 2;
    public static final Difficulty DIFFICULTY = Difficulty.MODERATE;
    public static final Long RATING_ID = 1L;
    public static final String DIRECTIONS = "Recipe directions";
    public static final String USER_NAME = "Jhon Test";
    public static final Long USER_ID = 4L;

    private RecipeDtoToRecipe converter;


    @Before
    public void setUp() throws Exception {
        converter = new RecipeDtoToRecipe(new RatingDtoToRating(new EvaluationDtoToEvaluation()),
                new CategoryDtoToCategory(), new IngredientDtoToIngredient(new UnitOfMeasureDtoToUnitOfMeasure()),
                new CommentDtoToComment());
    }

    @Test
    public void convert() throws Exception {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(ID_VALUE);
        recipeDTO.setCookTime(COOK_TIME);
        recipeDTO.setName(NAME);
        recipeDTO.setServings(SERVINGS);
        recipeDTO.setDifficulty(DIFFICULTY);
        recipeDTO.setDirections(DIRECTIONS);
        recipeDTO.setUserId(USER_ID);
        recipeDTO.setUserName(USER_NAME);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setRecipeId(ID_VALUE);
        recipeDTO.getComments().add(commentDTO);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("European");
        recipeDTO.getCategories().add(categoryDTO);

        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setId(RATING_ID);
        ratingDTO.setRecipeId(ID_VALUE);
        recipeDTO.setRating(ratingDTO);

        Recipe recipe = converter.convert(recipeDTO);

        assertNotNull(recipe);
        assertEquals(ID_VALUE, recipe.getId());
        assertEquals(COOK_TIME, recipe.getCookTime());
        assertEquals(SERVINGS, recipe.getServings());
        assertEquals(DIFFICULTY, recipe.getDifficulty());
        assertEquals(DIRECTIONS, recipe.getDirections());
        assertEquals(NAME, recipe.getName());
        assertEquals(USER_ID, recipe.getUser().getId());
        assertEquals(USER_NAME, recipe.getUserName());
        assertEquals(1, recipe.getComments().size());
        assertEquals(1,recipe.getCategories().size());
    }

}
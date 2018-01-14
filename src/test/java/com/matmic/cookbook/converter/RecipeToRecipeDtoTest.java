package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.*;
import com.matmic.cookbook.dto.RecipeDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecipeToRecipeDtoTest {
    public static final Long ID_VALUE = 1L;
    public static final String NAME = "Recipe name";
    public static final int COOK_TIME = 23;
    public static final int SERVINGS = 2;
    public static final Difficulty DIFFICULTY = Difficulty.MODERATE;
    public static final Long RATING_ID = 1L;
    public static final String DIRECTIONS = "Recipe directions";
    public static final String USER_NAME = "Jhon Test";
    public static final Long USER_ID = 4L;

    private RecipeToRecipeDto converter;

    @Before
    public void setUp() throws Exception {
        converter = new RecipeToRecipeDto(new RatingToRatingDto(new EvaluationToEvaluationDto()), new CategoryToCategoryDto(),
                new IngredientToIngredientDto(new UnitOfMeasureToUnitOfMeasureDto()), new CommentToCommentDto());
    }

    @Test
    public void convert() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(ID_VALUE);
        recipe.setCookTime(COOK_TIME);
        recipe.setName(NAME);
        recipe.setServings(SERVINGS);
        recipe.setDifficulty(DIFFICULTY);
        recipe.setDirections(DIRECTIONS);
        recipe.setUserName(USER_NAME);
        User user = new User();
        user.setId(USER_ID);
        recipe.setUser(user);

        Comment comment = new Comment();
        comment.setRecipe(recipe);
        comment.setUser(user);
        recipe.getComments().add(comment);

        Category category = new Category();
        category.setName("European");
        recipe.getCategories().add(category);

        Rating rating = new Rating();
        rating.setId(RATING_ID);
        rating.setRecipe(recipe);
        recipe.setRating(rating);

        RecipeDTO recipeDTO = converter.convert(recipe);

        assertNotNull(recipe);
        assertEquals(ID_VALUE, recipeDTO.getId());
        assertEquals(COOK_TIME, recipeDTO.getCookTime());
        assertEquals(SERVINGS, recipeDTO.getServings());
        assertEquals(DIFFICULTY, recipeDTO.getDifficulty());
        assertEquals(DIRECTIONS, recipeDTO.getDirections());
        assertEquals(NAME, recipeDTO.getName());
        assertEquals(USER_ID, recipeDTO.getUserId());
        assertEquals(USER_NAME, recipeDTO.getUserName());
        assertEquals(1, recipeDTO.getComments().size());
        assertEquals(1,recipeDTO.getCategories().size());
    }

}
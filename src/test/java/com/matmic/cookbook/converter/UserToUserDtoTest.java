package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.*;
import com.matmic.cookbook.dto.UserDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserToUserDtoTest {
    public static final Long ID_VALUE = 3L;
    public static final String NAME = "UserName";
    public static final String EMAIL = "user@email.com";
    public static final String PASSWORD = "userPassword";

    private UserToUserDto converter;

    @Before
    public void setUp() throws Exception {
        RecipeToRecipeDto recipeConv = new RecipeToRecipeDto(new RatingToRatingDto(new EvaluationToEvaluationDto()), new CategoryToCategoryDto(),
                new IngredientToIngredientDto(new UnitOfMeasureToUnitOfMeasureDto()), new CommentToCommentDto());
        converter = new UserToUserDto(new CommentToCommentDto(), new EvaluationToEvaluationDto(), recipeConv);
    }

    @Test
    public void convert() throws Exception {
        User user = new User();
        user.setId(ID_VALUE);
        user.setName(NAME);
        user.setEmail(EMAIL);

        Evaluation evaluation = new Evaluation();
        evaluation.setId(3L);
        evaluation.setUser(user);
        evaluation.setRating(new Rating());
        user.getEvaluations().add(evaluation);

        Recipe recipe = new Recipe();
        recipe.setId(2L);
        recipe.setUser(user);
        user.getRecipes().add(recipe);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUser(user);
        comment.setRecipe(recipe);
        user.getComments().add(comment);

        UserDTO userDTO = converter.convert(user);

        assertNotNull(userDTO);
        assertEquals(ID_VALUE, userDTO.getId());
        assertEquals(NAME, userDTO.getName());
        assertEquals(EMAIL, userDTO.getEmail());
        assertEquals(1, userDTO.getEvaluations().size());
        assertEquals(1, userDTO.getComments().size());
        assertEquals(1, userDTO.getRecipes().size());
    }

}
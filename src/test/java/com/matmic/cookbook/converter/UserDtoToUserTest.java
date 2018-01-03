package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.CommentDTO;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.dto.UserDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserDtoToUserTest {
    public static final Long ID_VALUE = 3L;
    public static final String NAME = "UserName";
    public static final String EMAIL = "user@email.com";
    public static final String PASSWORD = "userPassword";

    private UserDtoToUser converter;

    @Before
    public void setUp() throws Exception {
        RecipeDtoToRecipe recipeConv = new RecipeDtoToRecipe(new RatingDtoToRating(new EvaluationDtoToEvaluation()),
                new CategoryDtoToCategory(), new IngredientDtoToIngredient(new UnitOfMeasureDtoToUnitOfMeasure()),
                new CommentDtoToComment());

        converter = new UserDtoToUser(new CommentDtoToComment(), new EvaluationDtoToEvaluation()
        , recipeConv);
    }

    @Test
    public void convert() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(ID_VALUE);
        userDTO.setEmail(EMAIL);
        userDTO.setName(NAME);
        userDTO.setPassword(PASSWORD);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setUserId(ID_VALUE);
        commentDTO.setId(2L);
        userDTO.getComments().add(commentDTO);

        EvaluationDTO evaluationDTO = new EvaluationDTO();
        evaluationDTO.setId(4L);
        evaluationDTO.setUserId(ID_VALUE);
        userDTO.getEvaluations().add(evaluationDTO);

        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(12L);
        recipeDTO.setUserId(ID_VALUE);
        userDTO.getRecipes().add(recipeDTO);

        User user = converter.convert(userDTO);

        assertNotNull(user);
        assertEquals(ID_VALUE, user.getId());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(NAME, user.getName());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(1, user.getEvaluations().size());
        assertEquals(1, user.getComments().size());
        assertEquals(1, user.getRecipes().size());
    }

}
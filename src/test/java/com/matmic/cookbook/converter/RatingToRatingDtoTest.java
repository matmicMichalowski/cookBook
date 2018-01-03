package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.RatingDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RatingToRatingDtoTest {
    public static final Long ID_VALUE = 1L;
    public static final double TOTAL_RATING = 5.0;
    public static final int EVALUATION_SUM = 20;
    public static final Long RECIPE_ID = 4L;

    private RatingToRatingDto converter;

    @Before
    public void setUp() throws Exception {
        converter = new RatingToRatingDto(new EvaluationToEvaluationDto());
    }

    @Test
    public void convert() throws Exception {
        Rating rating = new Rating();
        rating.setId(ID_VALUE);
        rating.setEvaluationSum(EVALUATION_SUM);
        rating.setTotalRating(TOTAL_RATING);

        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        rating.setRecipe(recipe);

        Evaluation evaluation1 = new Evaluation();
        evaluation1.setId(1L);
        evaluation1.setRating(rating);
        evaluation1.setUser(new User());

        Evaluation evaluation2 = new Evaluation();
        evaluation2.setId(4L);
        evaluation2.setRating(rating);
        evaluation2.setUser(new User());

        rating.getUsersEvaluations().add(evaluation1);
        rating.getUsersEvaluations().add(evaluation2);

        RatingDTO ratingDTO = converter.convert(rating);

        assertNotNull(ratingDTO);
        assertEquals(ID_VALUE, ratingDTO.getId());
        assertEquals(Double.valueOf(TOTAL_RATING), Double.valueOf(ratingDTO.getTotalRating()));
        assertEquals(RECIPE_ID, ratingDTO.getRecipeId());
        assertEquals(EVALUATION_SUM, ratingDTO.getEvaluationSum());
        assertEquals(2, ratingDTO.getUsersEvaluations().size());

    }

}
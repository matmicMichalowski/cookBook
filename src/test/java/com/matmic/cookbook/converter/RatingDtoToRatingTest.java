package com.matmic.cookbook.converter;


import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.dto.RatingDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RatingDtoToRatingTest {

    public static final Long ID_VALUE = 1L;
    public static final String TOTAL_RATING = "5.00";
    public static final int EVALUATION_SUM = 20;
    public static final Long RECIPE_ID = 4L;

    private RatingDtoToRating converter;

    @Before
    public void setUp() throws Exception {
        converter = new RatingDtoToRating(new EvaluationDtoToEvaluation());
    }

    @Test
    public void convert() throws Exception {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setId(ID_VALUE);
        ratingDTO.setRecipeId(RECIPE_ID);
        ratingDTO.setTotalRating(TOTAL_RATING);
        ratingDTO.setEvaluationSum(EVALUATION_SUM);

        EvaluationDTO evaluation1 = new EvaluationDTO();
        evaluation1.setId(3L);

        EvaluationDTO evaluation2 = new EvaluationDTO();
        evaluation2.setId(6L);

        ratingDTO.getUsersEvaluations().add(evaluation1);
        ratingDTO.getUsersEvaluations().add(evaluation2);

        Rating rating = converter.convert(ratingDTO);

        assertNotNull(rating);
        assertEquals(ratingDTO.getId(), rating.getId());
        assertEquals(2, rating.getUsersEvaluations().size());
        assertEquals(Double.valueOf(TOTAL_RATING),Double.valueOf(rating.getTotalRating()));
        assertEquals(EVALUATION_SUM, rating.getEvaluationSum());
        assertEquals(RECIPE_ID, rating.getRecipe().getId());
    }

}
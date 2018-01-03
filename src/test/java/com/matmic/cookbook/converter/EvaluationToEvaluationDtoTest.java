package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.EvaluationDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EvaluationToEvaluationDtoTest {
    public static final Long ID_VALUE = 1L;
    public static final int SCORE = 5;
    public static final Long RATING_ID = 5L;
    public static final Long USER_ID = 4L;

    private EvaluationToEvaluationDto converter;

    @Before
    public void setUp() throws Exception {
        converter = new EvaluationToEvaluationDto();
    }

    @Test
    public void convert() throws Exception {
        Evaluation evaluation = new Evaluation();
        evaluation.setId(ID_VALUE);
        evaluation.setScore(SCORE);
        evaluation.setRating(new Rating());
        evaluation.getRating().setId(RATING_ID);
        evaluation.setUser(new User());
        evaluation.getUser().setId(USER_ID);

        EvaluationDTO evaluationDTO = converter.convert(evaluation);

        assertNotNull(evaluationDTO);
        assertEquals(evaluation.getId(), evaluationDTO.getId());
        assertEquals(evaluation.getScore(), evaluationDTO.getScore());
        assertEquals(evaluation.getUser().getId(), evaluationDTO.getUserId());
        assertEquals(evaluation.getRating().getId(), evaluationDTO.getRatingId());

    }

}
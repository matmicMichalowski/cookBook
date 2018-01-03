package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.dto.EvaluationDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EvaluationDtoToEvaluationTest {

    public static final Long ID_VALUE = 1L;
    public static final int SCORE = 5;
    public static final Long RATING_ID = 5L;
    public static final Long USER_ID = 4L;


    EvaluationDtoToEvaluation converter;


    @Before
    public void setUp() throws Exception {
        converter = new EvaluationDtoToEvaluation();

    }

    @Test
    public void convert() throws Exception {
        EvaluationDTO evaluationDTO = new EvaluationDTO();
        evaluationDTO.setId(ID_VALUE);
        evaluationDTO.setUserId(USER_ID);
        evaluationDTO.setScore(SCORE);
        evaluationDTO.setRatingId(RATING_ID);

        Evaluation evaluation = converter.convert(evaluationDTO);

        assertNotNull(evaluation);
        assertEquals(evaluationDTO.getId(), evaluation.getId());
        assertEquals(evaluationDTO.getScore(), evaluation.getScore());
        assertEquals(evaluationDTO.getUserId(), evaluation.getUser().getId());
        assertEquals(evaluationDTO.getRatingId(), evaluation.getRating().getId());
    }

}
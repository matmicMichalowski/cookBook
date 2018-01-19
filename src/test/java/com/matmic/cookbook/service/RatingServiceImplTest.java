package com.matmic.cookbook.service;


import com.matmic.cookbook.converter.EvaluationDtoToEvaluation;
import com.matmic.cookbook.converter.EvaluationToEvaluationDto;
import com.matmic.cookbook.converter.RatingToRatingDto;
import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.dto.RatingDTO;
import com.matmic.cookbook.repository.RatingRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;


    private RatingToRatingDto toRatingDto = new RatingToRatingDto(new EvaluationToEvaluationDto());

    private EvaluationDtoToEvaluation toEvaluation = new EvaluationDtoToEvaluation();


    private RatingService ratingService;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ratingService = new RatingServiceImpl(toEvaluation, toRatingDto, ratingRepository);
    }

    @Test
    public void updateRatingNewEvaluation() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setId(4L);

        Rating rating = new Rating();
        rating.setId(1L);
        rating.setEvaluationSum(15);
        rating.setTotalRating(5.0);
        rating.setRecipe(recipe);


        EvaluationDTO evaluation = new EvaluationDTO();
        evaluation.setId(2L);
        evaluation.setScore(4);
        evaluation.setRatingId(1L);
        evaluation.setUserId(4L);

        when(ratingRepository.findById(anyLong())).thenReturn(Optional.of(rating));
        when(ratingRepository.save(any())).thenReturn(rating);

        RatingDTO saved = ratingService.updateRating(evaluation);

        assertNotNull(saved);
        assertEquals(19, saved.getEvaluationSum());
        verify(ratingRepository, times(1)).findById(anyLong());
        verify(ratingRepository, times(1)).save(any());

    }@Test
    public void updateRating() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setId(4L);

        Rating rating = new Rating();
        rating.setId(1L);
        rating.setEvaluationSum(15);
        rating.setTotalRating(5.0);
        rating.setRecipe(recipe);

        Evaluation evaluationToUpdate = new Evaluation();
        evaluationToUpdate.setId(2L);
        evaluationToUpdate.setUser(new User());
        evaluationToUpdate.getUser().setId(4L);
        evaluationToUpdate.setScore(3);
        evaluationToUpdate.setRating(rating);
        rating.getUsersEvaluations().add(evaluationToUpdate);

        EvaluationDTO evaluation = new EvaluationDTO();
        evaluation.setId(2L);
        evaluation.setScore(1);
        evaluation.setRatingId(1L);
        evaluation.setUserId(4L);

        when(ratingRepository.findById(anyLong())).thenReturn(Optional.of(rating));
        when(ratingRepository.save(any())).thenReturn(rating);

        RatingDTO saved = ratingService.updateRating(evaluation);

        assertNotNull(saved);
        assertEquals(1, saved.getUsersEvaluations().size());
        assertEquals(13, saved.getEvaluationSum());
        assertEquals(evaluationToUpdate.getScore(), evaluation.getScore());
        verify(ratingRepository, times(1)).findById(anyLong());
        verify(ratingRepository, times(1)).save(any());

    }


}
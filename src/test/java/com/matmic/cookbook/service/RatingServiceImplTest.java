package com.matmic.cookbook.service;

import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.dto.RatingDTO;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.dto.UserDTO;
import com.matmic.cookbook.mapper.EvaluationMapper;
import com.matmic.cookbook.mapper.RatingMapper;
import com.matmic.cookbook.mapper.RecipeMapper;
import com.matmic.cookbook.repository.RatingRepository;
import com.matmic.cookbook.repository.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingMapper ratingMapper;

    @Mock
    private RecipeMapper recipeMapper;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private EvaluationMapper evaluationMapper;

    private RatingService ratingService;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ratingService = new RatingServiceImpl(recipeRepository, ratingMapper, evaluationMapper, ratingRepository);
    }

    @Test
    public void saveAndUpdateRating() throws Exception {
        RecipeDTO recipe = new RecipeDTO();
        recipe.setId(3L);

        Recipe recipeFound = recipeMapper.recipeDtoToRecipe(recipe);

        RatingDTO rating = new RatingDTO();
        rating.setId(1L);
        rating.setRecipe(recipe);
        rating.setUsersEvaluations(new HashSet<>());
        rating.setEvaluationSum(15);
        rating.setTotalRating(5.0);

        recipe.setRating(rating);

        EvaluationDTO evaluation = new EvaluationDTO();
        evaluation.setId(2L);
        evaluation.setRecipe(recipe);
        evaluation.setUser(new UserDTO());
        evaluation.setScore(4);

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipeFound));

        when(ratingRepository.save(any())).thenReturn(rating);

        RatingDTO saved = ratingService.saveAndUpdateRating(evaluation);



        assertEquals(19, saved.getEvaluationSum());

    }

    @Test
    public void findRatingByRecipe() throws Exception {
    }

}
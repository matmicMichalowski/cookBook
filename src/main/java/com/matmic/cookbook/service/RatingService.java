package com.matmic.cookbook.service;

import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.dto.RatingDTO;

import java.util.Set;

public interface RatingService {

    RatingDTO updateRating(EvaluationDTO evaluationDTO);
    RatingDTO findRatingByRecipe(Long recipeId);
    Set<EvaluationDTO> findRatingEvaluations(Long ratingId);
}

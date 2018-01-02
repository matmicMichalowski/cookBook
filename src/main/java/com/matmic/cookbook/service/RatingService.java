package com.matmic.cookbook.service;

import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.dto.RatingDTO;

public interface RatingService {

    RatingDTO saveAndUpdateRating(EvaluationDTO evaluationDTO);
    RatingDTO findRatingByRecipe(Long recipeId);
}

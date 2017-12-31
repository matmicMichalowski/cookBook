package com.matmic.cookbook.service;

import com.matmic.cookbook.dto.RatingDTO;

import java.util.List;

public interface RatingService {

    RatingDTO saveOrUpdateRating(RatingDTO ratingDTO);
    RatingDTO findRatingByRecipe(Long recipeId);
    List<RatingDTO> findUserRatings(Long userId);
}

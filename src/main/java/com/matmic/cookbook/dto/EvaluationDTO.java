package com.matmic.cookbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EvaluationDTO {

    private Long id;
    private UserDTO user;
    private int score;
    private RecipeDTO recipe;
    private RatingDTO recipeRating;
}

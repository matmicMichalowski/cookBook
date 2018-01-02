package com.matmic.cookbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {

    private Long id;
    private int evaluationSum;
    private double totalRating;
    private Set<EvaluationDTO> usersEvaluations;
    private RecipeDTO recipe;
}

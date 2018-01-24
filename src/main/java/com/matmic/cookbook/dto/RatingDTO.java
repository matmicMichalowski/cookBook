package com.matmic.cookbook.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {

    private Long id;
    private int evaluationSum;
    private String totalRating;
    private Set<EvaluationDTO> usersEvaluations = new HashSet<>();

    @ApiModelProperty(required = true)
    private Long recipeId;
}

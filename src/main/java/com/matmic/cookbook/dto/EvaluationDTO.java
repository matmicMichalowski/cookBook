package com.matmic.cookbook.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EvaluationDTO {

    private Long id;

    @ApiModelProperty(required = true)
    private Long userId;

    @ApiModelProperty(required = true)
    private int score;

    @ApiModelProperty(required = true)
    private Long ratingId;

}

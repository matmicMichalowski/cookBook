package com.matmic.cookbook.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngredientDTO {
    private Long id;

    @ApiModelProperty(required = true)
    private String name;

    @ApiModelProperty(required = true)
    private UnitOfMeasureDTO unitOfMeasure;

    @ApiModelProperty(required = true)
    private int amount;

    @ApiModelProperty(required = true)
    private Long recipeId;
}

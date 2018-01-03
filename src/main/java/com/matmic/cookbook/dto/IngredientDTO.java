package com.matmic.cookbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngredientDTO {
    private Long id;
    private String name;
    private UnitOfMeasureDTO unitOfMeasure;
    private int amount;
    private Long recipeId;
}

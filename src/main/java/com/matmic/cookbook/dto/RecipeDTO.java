package com.matmic.cookbook.dto;

import com.matmic.cookbook.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class RecipeDTO {

    private Long id;
    private String name;
    private int cookTime;
    private int servings;
    private Difficulty difficulty;
    private RatingDTO rating;
    private String directions;
    private UserDTO user;
    private Set<CategoryDTO> categories;
    private Set<IngredientDTO> ingredients;

}

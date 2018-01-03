package com.matmic.cookbook.dto;

import com.matmic.cookbook.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
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
    private Long userId;
    private Set<CategoryDTO> categories = new HashSet<>();
    private Set<CommentDTO> comments = new HashSet<>();
    private Set<IngredientDTO> ingredients = new HashSet<>();

}

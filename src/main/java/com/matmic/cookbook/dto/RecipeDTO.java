package com.matmic.cookbook.dto;

import com.matmic.cookbook.domain.Difficulty;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(required = true)
    private String name;

    @ApiModelProperty(required = true)
    private int cookTime;

    @ApiModelProperty(required = true)
    private int servings;

    @ApiModelProperty(required = true)
    private Difficulty difficulty;
    private String userName;

    private RatingDTO rating;

    @ApiModelProperty(required = true)
    private String directions;

    @ApiModelProperty(required = true)
    private Long userId;
    private Set<CategoryDTO> categories = new HashSet<>();
    private Set<CommentDTO> comments = new HashSet<>();
    private Set<IngredientDTO> ingredients = new HashSet<>();

}

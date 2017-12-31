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
    private double value;
    private Set<UserDTO> usersRatings;
    private RecipeDTO recipe;
}

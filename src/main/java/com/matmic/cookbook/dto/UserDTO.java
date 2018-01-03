package com.matmic.cookbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Set<RecipeDTO> recipes = new HashSet<>();
    private Set<CommentDTO> comments = new HashSet<>();
    private Set<EvaluationDTO> evaluations = new HashSet<>();
}

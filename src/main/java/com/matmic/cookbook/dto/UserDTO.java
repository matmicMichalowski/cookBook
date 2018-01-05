package com.matmic.cookbook.dto;

import com.matmic.cookbook.domain.Authority;
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
    private boolean isActive = false;
    private Set<Authority> authorities = new HashSet<>();
    private Set<RecipeDTO> recipes = new HashSet<>();
    private Set<CommentDTO> comments = new HashSet<>();
    private Set<EvaluationDTO> evaluations = new HashSet<>();
}

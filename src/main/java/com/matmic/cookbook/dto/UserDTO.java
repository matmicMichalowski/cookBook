package com.matmic.cookbook.dto;

import com.matmic.cookbook.domain.User;
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
    private boolean isActive = false;
    private Set<String> authorities = new HashSet<>();
    private Set<RecipeDTO> recipes = new HashSet<>();
    private Set<CommentDTO> comments = new HashSet<>();
    private Set<EvaluationDTO> evaluations = new HashSet<>();

    public UserDTO(Long id, String name, String email, Set<String> authorities){

        this.id = id;
        this.name = name;
        this.email = email;
        this.authorities = authorities;
    }

    public UserDTO(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.isActive = user.isActive();

    }
}

package com.matmic.cookbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {

    private Long id;

    private String comment;
    private Long userId;
    private Long recipeId;
}

package com.matmic.cookbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {

    private Long id;

    @Size(min = 5, max = 500)
    private String comment;


    private String userName;

    @NotNull
    @Min(1)
    private Long userId;

    @NotNull
    @Min(1)
    private Long recipeId;
}

package com.matmic.cookbook.dto;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(required = true)
    @Size(min = 5, max = 500)
    private String comment;

    @ApiModelProperty(required = true)
    private String userName;

    @NotNull
    @Min(1)
    @ApiModelProperty(required = true)
    private Long userId;

    @NotNull
    @Min(1)
    @ApiModelProperty(required = true)
    private Long recipeId;
}

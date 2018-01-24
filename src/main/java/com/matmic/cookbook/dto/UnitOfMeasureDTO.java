package com.matmic.cookbook.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UnitOfMeasureDTO {

    private Long id;

    @ApiModelProperty(required = true)
    private String name;
}

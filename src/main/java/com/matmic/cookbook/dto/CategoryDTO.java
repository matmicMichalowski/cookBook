package com.matmic.cookbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {

    private Long id;

    @Size(min = 2, max = 50)
    private String name;

    public void setName(String name){
        this.name = name.toLowerCase();
    }
}

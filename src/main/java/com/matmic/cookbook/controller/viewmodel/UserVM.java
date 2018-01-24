package com.matmic.cookbook.controller.viewmodel;

import com.matmic.cookbook.dto.UserDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
public class UserVM extends UserDTO{

    @ApiModelProperty(required = true)
    @Size(min = 5, max = 100)
    private String password;

    public UserVM(Long id, String name, String password, String email, Set<String> authorities){
        super(id, name, email, authorities);
        this.password = password;
    }
}

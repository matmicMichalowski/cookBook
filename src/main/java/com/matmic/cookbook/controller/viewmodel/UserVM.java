package com.matmic.cookbook.controller.viewmodel;

import com.matmic.cookbook.dto.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
public class UserVM extends UserDTO{

    @Size(min = 5, max = 100)
    private String password;

    public UserVM(Long id, String name, String password, String email, boolean isActive, Set<String> authorities){
        super(id, name, email, isActive, authorities);
        this.password = password;
    }
}

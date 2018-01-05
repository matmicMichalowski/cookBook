package com.matmic.cookbook.service;


import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<EvaluationDTO> findUserEvaluations(Long userId);
    UserDTO saveUser(UserDTO userDTO);
    UserDTO findUserDTOByID(Long id);
    User findUserByID(Long id);
    List<RecipeDTO> findUserRecipes(Long userId);
    UserDTO findUserByUsername(String username);
    void deleteUser(Long id);

}

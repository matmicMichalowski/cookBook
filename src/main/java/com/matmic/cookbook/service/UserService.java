package com.matmic.cookbook.service;

import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<Evaluation> findUserEvaluations(Long userId);
    UserDTO saveUser(UserDTO userDTO);
    UserDTO findUserDTOByID(Long id);
    User findUserByID(Long id);
    UserDTO findUserByUsername(String username);
    void deleteUser(Long id);

}

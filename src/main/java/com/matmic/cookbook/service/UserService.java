package com.matmic.cookbook.service;


import com.matmic.cookbook.controller.viewmodel.UserVM;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<EvaluationDTO> findUserEvaluations(Long userId);
    Page<UserDTO> findAllUsers(Pageable pageable);
    Optional<User> activateUser(String activationToken);
    Optional<User> resetPasswordRequest(String userEmail);
    Optional<User> completeResetPasswordRequest(String newPassword, String resetToken);
    void changePassword(String password);
    User createUser(UserVM userVM);
    User createUser(String name, String email, String password);
    UserDTO updateUser(UserDTO userDTO);
    UserDTO findUserDTOByID(Long id);
    User findUserByID(Long id);
    List<RecipeDTO> findUserRecipes(Long userId);
    UserDTO findUserByUsername(String username);
    List<String> getAuthorities();
    boolean checkIsAdmin(String username);
    User getUserWithAuthorities();
    void deleteUser(Long id);

}

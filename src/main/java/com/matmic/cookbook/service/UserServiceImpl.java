package com.matmic.cookbook.service;

import com.matmic.cookbook.controller.viewmodel.UserVM;
import com.matmic.cookbook.converter.EvaluationToEvaluationDto;
import com.matmic.cookbook.converter.RecipeToRecipeDto;
import com.matmic.cookbook.converter.UserDtoToUser;
import com.matmic.cookbook.converter.UserToUserDto;
import com.matmic.cookbook.domain.Authority;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.dto.UserDTO;
import com.matmic.cookbook.repository.AuthorityRepository;
import com.matmic.cookbook.repository.UserRepository;
import com.matmic.cookbook.security.AuthoritiesConstants;
import com.matmic.cookbook.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing User
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserToUserDto toUserDto;
    private final UserDtoToUser toUser;
    private final RecipeToRecipeDto toRecipeDto;
    private final EvaluationToEvaluationDto toEvaluationDto;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public UserServiceImpl(UserRepository userRepository, UserToUserDto toUserDto,
                           UserDtoToUser toUser, RecipeToRecipeDto toRecipeDto,
                           EvaluationToEvaluationDto toEvaluationDto, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.toUserDto = toUserDto;
        this.toUser = toUser;
        this.toEvaluationDto = toEvaluationDto;
        this.toRecipeDto = toRecipeDto;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }


    /**
     * Get all User evaluations
     *
     * @param userId the id of user
     * @return list of user evaluationDTO
     */
    @Override
    public List<EvaluationDTO> findUserEvaluations(Long userId) {
        return findUserByID(userId).getEvaluations().stream()
                .map(toEvaluationDto::convert)
                .collect(Collectors.toList());
    }

    /**
     * Get all users
     *
     * @param pageable pagination information
     * @return list of entities
     */
    @Override
    public Page<UserDTO> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(toUserDto::convert);
    }

    /**
     * Activation user account
     *
     * @param activationToken token to enable activation
     * @return optional of user with activated account
     */
    @Override
    public Optional<User> activateUser(String activationToken) {
        return userRepository.findOneByActivationToken(activationToken)
                .map(user -> {
                    user.setActive(true);
                    user.setActivationToken(null);
                    return user;
                });
    }

    /**
     * Request to generate email with reset password token
     * @param userEmail user email
     * @return optional of user entity found by email
     */
    @Override
    public Optional<User> resetPasswordRequest(String userEmail) {
        return userRepository.findOneByEmail(userEmail).filter(User::isActive)
                .map(user -> {
                    user.setResetToken(UUID.randomUUID().toString());
                    return user;
                });
    }

    /**
     * Complete reset password request
     *
     * @param newPassword new password
     * @param resetToken reset password token
     * @return optional of user entity
     */
    @Override
    public Optional<User> completeResetPasswordRequest(String newPassword, String resetToken) {
        return userRepository.findOneByResetToken(resetToken)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetToken(null);
                    return user;
                });
    }

    /**
     * Change password
     *
     * @param password new password
     */
    @Override
    public void changePassword(String password) {
        userRepository.findUserByName(SecurityUtil.getCurrentUser()).ifPresent(
                user -> {
                    String encryptedPassword = passwordEncoder.encode(password);
                    user.setPassword(encryptedPassword);
                });
    }

    /**
     * Create new User
     *
     * @param userVM user View Model to create new User
     * @return created and saved User
     */
    @Override
    public User createUser(UserVM userVM) {
        User newUser = new User();

        if (userRepository.count() == 0){
            newUser.getAuthorities().add(authorityRepository.findOneByName(AuthoritiesConstants.ADMIN));
        }else{
            newUser.getAuthorities().add(authorityRepository.findOneByName(AuthoritiesConstants.USER));
        }

        String encryptedPassword = passwordEncoder.encode(userVM.getPassword());
        newUser.setPassword(encryptedPassword);
        newUser.setEmail(userVM.getEmail());
        newUser.setName(userVM.getName());
        newUser.setActivationToken(UUID.randomUUID().toString());
        userRepository.save(newUser);
        log.debug("Created new userVM: {}", newUser);
        return newUser;
    }

    /**
     * Create new User
     *
     * @param name user name
     * @param email user email
     * @param password user password
     * @return created and saved User
     */
    @Override
    public User createUser(String name, String email, String password) {
        User user = new User();
        Authority authority = authorityRepository.findOneByName(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        user.setName(name);
        user.setPassword(encryptedPassword);
        user.setEmail(email);
        user.setActive(false);
        authorities.add(authority);
        user.setAuthorities(authorities);
        user.setActivationToken(UUID.randomUUID().toString());
        userRepository.saveAndFlush(user);
        log.debug("Created new user: {}", user);
        return user;
    }

    /**
     * Update existing User
     *
     * @param userDTO entity to be updated
     * @return updated userDTO
     */
    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        Optional<User> optional = userRepository.findById(userDTO.getId());
        if (optional.isPresent()) {
            User detachedUser = toUser.convert(userDTO);
            User savedUser = userRepository.save(detachedUser);
            log.debug("User updated: {}", userDTO.getId());
            return toUserDto.convert(savedUser);
        }
        return null;
    }

    /**
     * Get UserDTO by Id
     *
     * @param id entity id
     * @return userDTO found
     */
    @Override
    public UserDTO findUserDTOByID(Long id) {
        Optional<User> optional = userRepository.findById(id);

        if(optional.isPresent()){
            log.debug("User found.");
            return toUserDto.convert(optional.get());
        }
        log.debug("No such user in database.");
        return null;
    }

    /**
     * Get all User recipes
     *
     * @param userId user id
     * @return list of all user recipes
     */
    @Override
    public List<RecipeDTO> findUserRecipes(Long userId){
        return findUserByID(userId).getRecipes().stream()
                .map(toRecipeDto::convert)
                .collect(Collectors.toList());
    }

    /**
     * Get User by username
     *
     * @param username user login(username)
     * @return user found
     */
    @Override
    public UserDTO findUserByUsername(String username) {
        Optional<User> optional = userRepository.findUserByName(username);

        if (optional.isPresent()){
            return toUserDto.convert(optional.get());
        }
        return null;
    }

    /**
     * Get all authorities
     *
     * @return list of all authorities
     */
    @Override
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    /**
     * Get User with authorities
     *
     * @return user found, or null
     */
    @Override
    public User getUserWithAuthorities() {
        return userRepository.findOneWithAuthoritiesByName(SecurityUtil.getCurrentUser()).orElse(null);
    }

    /**
     * Check if User is ADMIN
     *
     * @param username user login(username)
     * @return true if user have ADMIN authority
     */
    @Override
    public boolean checkIsAdmin(String username){
        UserDTO user = findUserByUsername(username);
        return user.getAuthorities().contains(AuthoritiesConstants.ADMIN);
    }

    /**
     * Get User by id
     *
     * @param id user id
     * @return user found
     */
    @Override
    public User findUserByID(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            return user.get();
        }
        return null;
    }

    /**
     * Delete User by id
     *
     * @param id user id
     */
    @Override
    public void deleteUser(Long id) {
        User userToDelete = findUserByID(id);
        userRepository.delete(userToDelete);
    }
}

package com.matmic.cookbook.service;

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


    @Override
    public List<EvaluationDTO> findUserEvaluations(Long userId) {
        return findUserByID(userId).getEvaluations().stream()
                .map(toEvaluationDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(toUserDto::convert);
    }

    @Override
    public Optional<User> activateUser(String activationToken) {
        return userRepository.findOneByActivationToken(activationToken)
                .map(user -> {
                    user.setActive(true);
                    user.setActivationToken(null);
                    return user;
                });
    }

    @Override
    public Optional<User> resetPasswordRequest(String userEmail) {
        return userRepository.findOneByEmail(userEmail).filter(User::isActive)
                .map(user -> {
                    user.setResetToken(UUID.randomUUID().toString());
                    return user;
                });
    }

    @Override
    public Optional<User> completeResetPasswordRequest(String newPassword, String resetToken) {
        return userRepository.findOneByResetToken(resetToken)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetToken(null);
                    return user;
                });
    }

    @Override
    public void changePassword(String password) {
        userRepository.findUserByName(SecurityUtil.getCurrentUser()).ifPresent(
                user -> {
                    String encryptedPassword = passwordEncoder.encode(password);
                    user.setPassword(encryptedPassword);

                });
    }


    @Override
    public User createUser(User user) {
        User newUser = new User();

        if (userRepository.count() == 0){
            newUser.getAuthorities().add(authorityRepository.findOneByName("ADMIN"));
        }else{
            newUser.getAuthorities().add(authorityRepository.findOneByName("USER"));
        }

        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(encryptedPassword);
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setActivationToken(UUID.randomUUID().toString());
        userRepository.save(newUser);
        log.debug("Created new user: {}", newUser);
        return newUser;
    }

    @Override
    public User createUser(String name, String email, String password) {
        User user = new User();
        Authority authority = authorityRepository.findOneByName("USER");
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        user.setName(name);
        user.setPassword(encryptedPassword);
        user.setEmail(email);
        user.setActive(false);
        authorities.add(authority);
        user.setAuthorities(authorities);
        user.setActivationToken(UUID.randomUUID().toString());
        userRepository.save(user);
        log.debug("Created new user: {}", user);
        return user;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        Optional<User> optional = userRepository.findById(userDTO.getId());
        if (optional.isPresent()) {
            User detachedUser = toUser.convert(userDTO);
            User savedUser = userRepository.save(detachedUser);
            return toUserDto.convert(savedUser);
        }
        return null;
    }

    @Override
    public UserDTO findUserDTOByID(Long id) {
        Optional<User> optional = userRepository.findById(id);

        if(optional.isPresent()){
            return toUserDto.convert(optional.get());
        }
        return null;
    }

    @Override
    public List<RecipeDTO> findUserRecipes(Long userId){
        return findUserByID(userId).getRecipes().stream()
                .map(toRecipeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findUserByUsername(String username) {
        Optional<User> optional = userRepository.findUserByName(username);

        if (optional.isPresent()){
            return toUserDto.convert(optional.get());
        }
        return null;
    }

    @Override
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    @Override
    public User getUserWithAuthorities() {
        return userRepository.findOneWithAuthoritiesByName(SecurityUtil.getCurrentUser()).orElse(null);
    }


    @Override
    public User findUserByID(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            return user.get();
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        User userToDelete = findUserByID(id);
        userRepository.delete(userToDelete);
    }
}

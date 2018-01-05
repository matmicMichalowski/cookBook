package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.EvaluationToEvaluationDto;
import com.matmic.cookbook.converter.RecipeToRecipeDto;
import com.matmic.cookbook.converter.UserDtoToUser;
import com.matmic.cookbook.converter.UserToUserDto;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.dto.UserDTO;
import com.matmic.cookbook.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserToUserDto toUserDto;
    private final UserDtoToUser toUser;
    private final RecipeToRecipeDto toRecipeDto;
    private final EvaluationToEvaluationDto toEvaluationDto;

    public UserServiceImpl(UserRepository userRepository, UserToUserDto toUserDto, UserDtoToUser toUser, RecipeToRecipeDto toRecipeDto, EvaluationToEvaluationDto toEvaluationDto) {
        this.userRepository = userRepository;
        this.toUserDto = toUserDto;
        this.toUser = toUser;
        this.toEvaluationDto = toEvaluationDto;
        this.toRecipeDto = toRecipeDto;
    }


    @Override
    public List<EvaluationDTO> findUserEvaluations(Long userId) {
        return findUserByID(userId).getEvaluations().stream()
                .map(toEvaluationDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        User detachedUser = toUser.convert(userDTO);

        User savedUser = userRepository.save(detachedUser);

        return toUserDto.convert(savedUser);
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

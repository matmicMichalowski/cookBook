package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.UserDtoToUser;
import com.matmic.cookbook.converter.UserToUserDto;
import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.UserDTO;
import com.matmic.cookbook.repository.EvaluationRepository;
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
    private final EvaluationRepository evaluationRepository;
    private final UserToUserDto toUserDto;
    private final UserDtoToUser toUser;

    public UserServiceImpl(UserRepository userRepository, EvaluationRepository evaluationRepository, UserToUserDto toUserDto, UserDtoToUser toUser) {
        this.userRepository = userRepository;
        this.toUserDto = toUserDto;
        this.toUser = toUser;
        this.evaluationRepository = evaluationRepository;
    }


    @Override
    public List<Evaluation> findUserEvaluations(Long userId) {
        return evaluationRepository.findAll().stream()
                .filter(evaluation -> evaluation.getUser().getId().equals(userId))
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

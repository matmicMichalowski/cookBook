package com.matmic.cookbook.service;

import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.UserDTO;
import com.matmic.cookbook.mapper.UserMapper;
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
    private final UserMapper userMapper;
    private final EvaluationRepository evaluationRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, EvaluationRepository evaluationRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
        User detachedUser = userMapper.userDtoToUser(userDTO);

        User savedUser = userRepository.save(detachedUser);

        return userMapper.userToUserDto(savedUser);
    }

    @Override
    public UserDTO findUserByID(Long id) {
        Optional<User> optional = userRepository.findById(id);

        if(optional.isPresent()){
            return userMapper.userToUserDto(optional.get());
        }

        return null;
    }

    @Override
    public UserDTO findUserByUsername(String username) {
        Optional<User> optional = userRepository.findUserByName(username);

        if (optional.isPresent()){
            return userMapper.userToUserDto(optional.get());
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        User userToDelete = userMapper.userDtoToUser(findUserByID(id));

        userRepository.delete(userToDelete);
    }
}

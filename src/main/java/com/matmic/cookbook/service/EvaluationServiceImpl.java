package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.EvaluationToEvaluationDto;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.repository.EvaluationRepository;
import com.matmic.cookbook.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Evaluation
 */
@Service
@Transactional
public class EvaluationServiceImpl implements EvaluationService {

    private final Logger log = LoggerFactory.getLogger(EvaluationServiceImpl.class);

    private final EvaluationToEvaluationDto toEvaluationDto;
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;


    public EvaluationServiceImpl(EvaluationToEvaluationDto toEvaluationDto, EvaluationRepository evaluationRepository, UserRepository userRepository) {
        this.toEvaluationDto = toEvaluationDto;
        this.evaluationRepository = evaluationRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all evaluations
     *
     * @return list of all evaluations
     */
    @Override
    @Transactional(readOnly = true)
    public List<EvaluationDTO> getEvaluations() {
        log.debug("Request to get all Evaluations");
        return evaluationRepository.findAll().stream()
                .map(toEvaluationDto::convert)
                .collect(Collectors.toList());
    }

    /**
     * Get one Evaluation by id
     * @param evaluationId entity id
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EvaluationDTO findEvaluationById(Long evaluationId) {
        log.debug("Request to get one Evaluation: {}", evaluationId);
        return evaluationRepository.findById(evaluationId).map(toEvaluationDto::convert)
                .orElseThrow(RuntimeException::new);
    }

    /**
     * Get evaluations by User
     *
     * @param userId the id of the user
     * @return list of all evaluations by User
     */
    @Override
    @Transactional(readOnly = true)
    public List<EvaluationDTO> evaluationsByUser(Long userId) {
        log.debug("Request to get evaluations by User: {}", userId);
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isPresent()){
            return optional.get().getEvaluations().stream()
                    .map(toEvaluationDto::convert)
                    .collect(Collectors.toList());
        }
        return null;
    }

}

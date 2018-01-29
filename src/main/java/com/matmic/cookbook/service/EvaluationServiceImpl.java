package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.EvaluationDtoToEvaluation;
import com.matmic.cookbook.converter.EvaluationToEvaluationDto;
import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.repository.EvaluationRepository;
import com.matmic.cookbook.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    private final EvaluationToEvaluationDto toEvaluationDto;
    private final EvaluationDtoToEvaluation toEvaluation;
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;


    public EvaluationServiceImpl(EvaluationToEvaluationDto toEvaluationDto, EvaluationDtoToEvaluation toEvaluation, EvaluationRepository evaluationRepository, UserRepository userRepository) {
        this.toEvaluationDto = toEvaluationDto;
        this.toEvaluation = toEvaluation;
        this.evaluationRepository = evaluationRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all evaluations
     *
     * @param pageable pagination information
     * @return list of all evaluations
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EvaluationDTO> getEvaluations(Pageable pageable) {
        return evaluationRepository.findAll(pageable).map(toEvaluationDto::convert);
    }

    /**
     * Get one Evaluation by id
     * @param evaluationId entity id
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EvaluationDTO findEvaluationById(Long evaluationId) {
        return evaluationRepository.findById(evaluationId).map(toEvaluationDto::convert)
                .orElseThrow(NullPointerException::new);
    }

    /**
     * Save new Evaluation
     *
     * @param evaluationDTO evaluation to be saved
     * @return saved entity
     */
    @Override
    public EvaluationDTO saveNewEvaluation(EvaluationDTO evaluationDTO){
        Evaluation detachedEvaluation = toEvaluation.convert(evaluationDTO);
        Evaluation evaluationSaved = evaluationRepository.save(detachedEvaluation);
        return toEvaluationDto.convert(evaluationSaved);
    }

    /**
     *  Update Evaluation entity
     *
     * @param evaluationDTO evaluation to update
     * @return updated and saved entity
     */
    @Override
    public EvaluationDTO updateEvaluation(EvaluationDTO evaluationDTO) {
        EvaluationDTO evaluationToUpdate = findEvaluationById(evaluationDTO.getId());
        if (evaluationDTO.getRatingId().equals(evaluationToUpdate.getRatingId()) &&
                evaluationDTO.getUserId().equals(evaluationToUpdate.getUserId())) {
            evaluationToUpdate.setScore(evaluationDTO.getScore());
            Evaluation updateEvaluation = evaluationRepository.save(toEvaluation.convert(evaluationToUpdate));
            return toEvaluationDto.convert(updateEvaluation);
        }
        return null;
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
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isPresent()){
            return optional.get().getEvaluations().stream()
                    .map(toEvaluationDto::convert)
                    .collect(Collectors.toList());
        }
        return null;
    }

}

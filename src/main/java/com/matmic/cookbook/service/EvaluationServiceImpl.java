package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.EvaluationToEvaluationDto;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.dto.RatingDTO;
import com.matmic.cookbook.repository.EvaluationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationToEvaluationDto toEvaluationDto;
    private final EvaluationRepository evaluationRepository;
    private final RatingService ratingService;
    private final UserService userService;


    public EvaluationServiceImpl(EvaluationToEvaluationDto toEvaluationDto, EvaluationRepository evaluationRepository, RatingService ratingService, UserService userService) {
        this.toEvaluationDto = toEvaluationDto;
        this.evaluationRepository = evaluationRepository;
        this.ratingService = ratingService;
        this.userService = userService;
    }

    @Override
    public List<EvaluationDTO> getEvaluations() {
        return evaluationRepository.findAll().stream()
                .map(toEvaluationDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluationDTO findEvaluationById(Long evaluationId) {
        return evaluationRepository.findById(evaluationId).map(toEvaluationDto::convert)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public EvaluationDTO saveEvaluation(EvaluationDTO evaluationDTO) {
        RatingDTO rating = ratingService.saveAndUpdateRating(evaluationDTO);

        return rating.getUsersEvaluations().stream()
                .filter(ev -> ev.getUserId().equals(evaluationDTO.getUserId()))
                .findFirst().get();
    }

    @Override
    public List<EvaluationDTO> evaluationsByUser(Long userId) {
        return userService.findUserEvaluations(userId);

    }

    @Override
    public Set<EvaluationDTO> evaluationsByRecipeRating(Long ratingId) {
        return ratingService.findRatingEvaluations(ratingId);
    }
}

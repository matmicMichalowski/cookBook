package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.EvaluationDtoToEvaluation;
import com.matmic.cookbook.converter.RatingToRatingDto;
import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.dto.RatingDTO;
import com.matmic.cookbook.repository.RatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing Rating
 */
@Service
@Transactional
public class RatingServiceImpl implements RatingService {

    private final Logger log = LoggerFactory.getLogger(RatingServiceImpl.class);

    private final EvaluationDtoToEvaluation toEvaluation;
    private final RatingToRatingDto toRatingDto;
    private final RatingRepository ratingRepository;


    public RatingServiceImpl(EvaluationDtoToEvaluation toEvaluation, RatingToRatingDto toRatingDto, RatingRepository ratingRepository) {
        this.toEvaluation = toEvaluation;
        this.toRatingDto = toRatingDto;
        this.ratingRepository = ratingRepository;

    }

    /**
     * Save and Update Rating
     *
     * @param evaluationDTO evaluation entity
     * @return saved and updated Rating entity
     */
    @Override
    public RatingDTO saveAndUpdateRating(EvaluationDTO evaluationDTO) {
        log.debug("Request to update and save Rating with Evaluation: {}", evaluationDTO);
        Optional<Rating> optional = ratingRepository.findById(evaluationDTO.getRatingId());

        Rating ratingToSave = optional.get();
        Evaluation evaluationToAdd = toEvaluation.convert(evaluationDTO);

        int evaluationSum = ratingToSave.getEvaluationSum();

        Optional<Evaluation> optionalEvaluation =  ratingToSave.getUsersEvaluations().stream()
                .filter(evaluation -> evaluation.getId().equals(evaluationDTO.getId()))
                .filter(evaluation -> evaluation.getUser().getId().equals(evaluationDTO.getUserId()))
                .findFirst();

        if (!optionalEvaluation.isPresent()){
            ratingToSave.getUsersEvaluations().add(evaluationToAdd);
            evaluationSum += evaluationToAdd.getScore();
        }else{
            Evaluation evaluationFound = optionalEvaluation.get();
            evaluationSum = evaluationSum - (evaluationFound.getScore() - evaluationDTO.getScore());
            evaluationFound.setScore(evaluationDTO.getScore());
        }
        ratingToSave.setEvaluationSum(evaluationSum);
        ratingToSave.setTotalRating((double)evaluationSum / (double)ratingToSave.getUsersEvaluations().size());

        Rating savedRating = ratingRepository.save(ratingToSave);

        return toRatingDto.convert(savedRating);
    }

    /**
     * Get evaluations from singe Rating
     *
     * @param ratingId id of Rating entity
     * @return set of evaluations
     */
    @Override
    @Transactional(readOnly = true)
    public Set<EvaluationDTO> findRatingEvaluations(Long ratingId){
        log.debug("Request for evaluations in Rating: {}", ratingId);
        return findRatingByRecipe(ratingId).getUsersEvaluations();
    }

    /**
     * Get Rating from Recipe by Recipe id
     *
     * @param recipeId id of Recipe entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RatingDTO findRatingByRecipe(Long recipeId) {
        log.debug("Request to get Rating from Recipe: {}", recipeId);
        Optional<Rating> optional = ratingRepository.findById(recipeId);

        if (optional.isPresent()){
            return toRatingDto.convert(optional.get());
        }
        return null;
    }

}

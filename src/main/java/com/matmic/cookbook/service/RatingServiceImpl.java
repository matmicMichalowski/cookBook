package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.EvaluationDtoToEvaluation;
import com.matmic.cookbook.converter.RatingToRatingDto;
import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.dto.RatingDTO;
import com.matmic.cookbook.repository.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RatingServiceImpl implements RatingService {


    private final EvaluationDtoToEvaluation toEvaluation;
    private final RatingToRatingDto toRatingDto;
    private final RatingRepository ratingRepository;


    public RatingServiceImpl(EvaluationDtoToEvaluation toEvaluation, RatingToRatingDto toRatingDto, RatingRepository ratingRepository) {
        this.toEvaluation = toEvaluation;
        this.toRatingDto = toRatingDto;
        this.ratingRepository = ratingRepository;

    }


    @Override
    public RatingDTO saveAndUpdateRating(EvaluationDTO evaluationDTO) {

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

    @Override
    public Set<EvaluationDTO> findRatingEvaluations(Long ratingId){
        return findRatingByRecipe(ratingId).getUsersEvaluations();
    }


    @Override
    public RatingDTO findRatingByRecipe(Long recipeId) {
        Optional<Rating> optional = ratingRepository.findById(recipeId);

        if (optional.isPresent()){
            return toRatingDto.convert(optional.get());
        }
        return null;
    }

}

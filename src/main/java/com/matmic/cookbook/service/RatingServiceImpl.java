package com.matmic.cookbook.service;

import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.dto.RatingDTO;
import com.matmic.cookbook.mapper.EvaluationMapper;
import com.matmic.cookbook.mapper.RatingMapper;
import com.matmic.cookbook.repository.RatingRepository;
import com.matmic.cookbook.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RatingServiceImpl implements RatingService {

    private final RecipeRepository recipeRepository;
    private final RatingMapper ratingMapper;
    private final EvaluationMapper evaluationMapper;
    private final RatingRepository ratingRepository;


    public RatingServiceImpl(RecipeRepository recipeRepository, RatingMapper ratingMapper, EvaluationMapper evaluationMapper, RatingRepository ratingRepository) {
        this.recipeRepository = recipeRepository;
        this.ratingMapper = ratingMapper;
        this.evaluationMapper = evaluationMapper;
        this.ratingRepository = ratingRepository;
    }


    @Override
    public RatingDTO saveAndUpdateRating(EvaluationDTO evaluationDTO) {
        Optional<Recipe> optional = recipeRepository.findById(evaluationDTO.getRecipe().getId());

//        if (!optional.isPresent()){
//            return null;
//        }

        Rating ratingToSave = optional.get().getRating();

        Evaluation evaluationToAdd = evaluationMapper.evaluationDtoToEvaluation(evaluationDTO);

        int evaluationSum = ratingToSave.getEvaluationSum();

        if (!ratingToSave.getUsersEvaluations().contains(evaluationToAdd)){
            ratingToSave.getUsersEvaluations().add(evaluationToAdd);
            evaluationSum += evaluationToAdd.getScore();
        }else{
           Optional<Evaluation> optionalEvaluation =  ratingToSave.getUsersEvaluations().stream()
                    .filter(evaluation -> evaluation.getId().equals(evaluationDTO.getId()))
                    .filter(evaluation -> evaluation.getRecipe().getId().equals(evaluationDTO.getRecipe().getId()))
                    .filter(evaluation -> evaluation.getUser().getId().equals(evaluationDTO.getUser().getId()))
                    .findFirst();

           if(optionalEvaluation.isPresent()){
               Evaluation evaluationFound = optionalEvaluation.get();
               evaluationFound.setScore(evaluationDTO.getScore());
               evaluationSum = evaluationFound.getScore() + evaluationDTO.getScore();
           }
        }


        ratingToSave.setEvaluationSum(evaluationSum);
        ratingToSave.setTotalRating((double)evaluationSum / (double)ratingToSave.getUsersEvaluations().size());


        Rating savedRating = ratingRepository.save(ratingToSave);

        return ratingMapper.ratingToRatingDto(savedRating);
    }


    @Override
    public RatingDTO findRatingByRecipe(Long recipeId) {
        //Optional<Rating> optional = ratingRepository.findByRecipe(recipeId);

//        if (optional.isPresent()){
//            return ratingMapper.ratingToRatingDto(optional.get());
//        }
        return null;
    }

}

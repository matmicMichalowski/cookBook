package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.dto.RatingDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RatingDtoToRating implements Converter<RatingDTO, Rating>{


    private final EvaluationDtoToEvaluation evaluationConverter;

    public RatingDtoToRating(EvaluationDtoToEvaluation evaluationConverter) {
        this.evaluationConverter = evaluationConverter;
    }


    @Synchronized
    @Nullable
    @Override
    public Rating convert(RatingDTO ratingDTO) {
        if(ratingDTO == null) {
            return null;
        }

        final Rating rating = new Rating();
        rating.setId(ratingDTO.getId());
        rating.setTotalRating(ratingDTO.getTotalRating());
        rating.setEvaluationSum(ratingDTO.getEvaluationSum());
        if (ratingDTO.getRecipeId() != null){
            Recipe recipe = new Recipe();
            recipe.setId(ratingDTO.getRecipeId());
            rating.setRecipe(recipe);
            recipe.setRating(rating);
        }

        if(ratingDTO.getUsersEvaluations() != null && ratingDTO.getUsersEvaluations().size() > 0) {
            ratingDTO.getUsersEvaluations().forEach(evaluationDTO ->
                    rating.getUsersEvaluations().add(evaluationConverter.convert(evaluationDTO)));
        }
        return rating;
    }
}

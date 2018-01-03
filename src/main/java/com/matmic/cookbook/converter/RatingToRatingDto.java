package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.dto.RatingDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RatingToRatingDto implements Converter<Rating, RatingDTO>{


    private final EvaluationToEvaluationDto evaluationConverter;

    public RatingToRatingDto(EvaluationToEvaluationDto evaluationConverter) {
        this.evaluationConverter = evaluationConverter;
    }


    @Synchronized
    @Nullable
    @Override
    public RatingDTO convert(Rating rating) {
        if(rating == null) {
            return null;
        }

        final RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setId(rating.getId());
        ratingDTO.setTotalRating(rating.getTotalRating());
        ratingDTO.setEvaluationSum(rating.getEvaluationSum());
        ratingDTO.setRecipeId(rating.getRecipe().getId());

        if(rating.getUsersEvaluations() != null && rating.getUsersEvaluations().size() > 0) {
            rating.getUsersEvaluations().forEach(evaluation ->
                    ratingDTO.getUsersEvaluations().add(evaluationConverter.convert(evaluation)));
        }

        return ratingDTO;
    }
}

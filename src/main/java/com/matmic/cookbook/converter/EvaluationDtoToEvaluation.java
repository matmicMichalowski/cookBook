package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.EvaluationDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class EvaluationDtoToEvaluation implements Converter<EvaluationDTO, Evaluation>{


    @Synchronized
    @Nullable
    @Override
    public Evaluation convert(EvaluationDTO evaluationDTO) {
        if (evaluationDTO == null) {
            return null;
        }

        final Evaluation evaluation = new Evaluation();
        evaluation.setId(evaluationDTO.getId());
        evaluation.setScore(evaluationDTO.getScore());
        if (evaluationDTO.getUserId() != null){
            User user = new User();
            user.setId(evaluationDTO.getUserId());
            evaluation.setUser(user);
            user.getEvaluations().add(evaluation);
        }
        if (evaluationDTO.getRatingId() != null){
            Rating rating = new Rating();
            rating.setId(evaluationDTO.getRatingId());
            evaluation.setRating(rating);
            rating.getUsersEvaluations().add(evaluation);
        }


        return evaluation;
    }
}

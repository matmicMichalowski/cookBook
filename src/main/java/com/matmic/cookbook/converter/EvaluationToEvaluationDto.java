package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.dto.EvaluationDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class EvaluationToEvaluationDto implements Converter<Evaluation, EvaluationDTO>{


    @Synchronized
    @Nullable
    @Override
    public EvaluationDTO convert(Evaluation evaluation) {
        if (evaluation == null) {
            return null;
        }

        final EvaluationDTO evaluationDTO = new EvaluationDTO();
        evaluationDTO.setId(evaluation.getId());
        evaluationDTO.setScore(evaluation.getScore());
        evaluationDTO.setRatingId(evaluation.getRating().getId());
        evaluationDTO.setUserId(evaluation.getUser().getId());
        return evaluationDTO;
    }
}


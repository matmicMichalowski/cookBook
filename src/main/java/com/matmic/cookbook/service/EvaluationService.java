package com.matmic.cookbook.service;


import com.matmic.cookbook.dto.EvaluationDTO;

import java.util.List;
import java.util.Set;

public interface EvaluationService {

    List<EvaluationDTO> getEvaluations();
    EvaluationDTO findEvaluationById(Long evaluationId);
    EvaluationDTO saveEvaluation(EvaluationDTO evaluationDTO);
    List<EvaluationDTO> evaluationsByUser(Long userId);
    Set<EvaluationDTO> evaluationsByRecipeRating(Long ratingId);
}

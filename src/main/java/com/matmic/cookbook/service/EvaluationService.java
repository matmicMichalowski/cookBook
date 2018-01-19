package com.matmic.cookbook.service;


import com.matmic.cookbook.dto.EvaluationDTO;

import java.util.List;

public interface EvaluationService {

    List<EvaluationDTO> getEvaluations();
    EvaluationDTO findEvaluationById(Long evaluationId);
    EvaluationDTO saveNewEvaluation(EvaluationDTO evaluationDTO);
    EvaluationDTO updateEvaluation(EvaluationDTO evaluationDTO);
    List<EvaluationDTO> evaluationsByUser(Long userId);
}

package com.matmic.cookbook.service;


import com.matmic.cookbook.dto.EvaluationDTO;

import java.util.List;

public interface EvaluationService {

    List<EvaluationDTO> getEvaluations();
    EvaluationDTO findEvaluationById(Long evaluationId);
    List<EvaluationDTO> evaluationsByUser(Long userId);
}

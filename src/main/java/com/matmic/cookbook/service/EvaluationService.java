package com.matmic.cookbook.service;


import com.matmic.cookbook.dto.EvaluationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EvaluationService {

    Page<EvaluationDTO> getEvaluations(Pageable pageable);
    EvaluationDTO findEvaluationById(Long evaluationId);
    EvaluationDTO saveNewEvaluation(EvaluationDTO evaluationDTO);
    EvaluationDTO updateEvaluation(EvaluationDTO evaluationDTO);
    List<EvaluationDTO> evaluationsByUser(Long userId);
}

package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.util.HttpHeadersUtil;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.service.EvaluationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/evaluation")
public class EvaluationController {

    private static final String ENTITY_NAME = "evaluation";

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping
    public ResponseEntity<List<EvaluationDTO>> getAllEvaluations(){
        return new ResponseEntity<>(evaluationService.getEvaluations(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationDTO> getEvaluationById(@PathVariable Long id){
        EvaluationDTO evaluationDTO = evaluationService.findEvaluationById(id);
        return new ResponseEntity<>(evaluationDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<EvaluationDTO>> getEvaluationsByUser(@PathVariable Long userId){
        return new ResponseEntity<>(evaluationService.evaluationsByUser(userId), HttpStatus.OK);
    }

    @GetMapping("/{recipeRatingId}")
    public ResponseEntity<Set<EvaluationDTO>> getEvaluationsByRecipeRating(@PathVariable Long recipeRatingId){
        return new ResponseEntity<>(evaluationService.evaluationsByRecipeRating(recipeRatingId), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<EvaluationDTO> saveNewEvaluation(@RequestBody EvaluationDTO evaluationDTO){
        if (evaluationDTO.getId() != null){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "A new evaluation can not be created it already have an Id")).body(null);
        }
        EvaluationDTO evaluation = evaluationService.saveEvaluation(evaluationDTO);
        return ResponseEntity.ok().headers(HttpHeadersUtil.createdEntityAlert(ENTITY_NAME, evaluation.getId().toString())).body(evaluation);
    }

    @PutMapping("/update")
    public ResponseEntity<EvaluationDTO> updateEvaluation(@RequestBody EvaluationDTO evaluationDTO){
        if (evaluationDTO.getId() == null){
            return saveNewEvaluation(evaluationDTO);
        }
        EvaluationDTO evaluation = evaluationService.saveEvaluation(evaluationDTO);
        return ResponseEntity.ok().headers(HttpHeadersUtil.updateEntityAlert(ENTITY_NAME, evaluation.getId().toString())).body(evaluation);
    }
}

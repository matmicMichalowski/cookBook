package com.matmic.cookbook.controller;

import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.service.EvaluationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EvaluationController {

    private static final String ENTITY_NAME = "evaluation";

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping("/evaluations")
    public ResponseEntity<List<EvaluationDTO>> getAllEvaluations(){
        return new ResponseEntity<>(evaluationService.getEvaluations(), HttpStatus.OK);
    }

    @GetMapping("/evaluation/{id}")
    public ResponseEntity<EvaluationDTO> getEvaluationById(@PathVariable Long id){
        EvaluationDTO evaluationDTO = evaluationService.findEvaluationById(id);
        return new ResponseEntity<>(evaluationDTO, HttpStatus.OK);
    }

    @GetMapping("/evaluation/user/{userId}")
    public ResponseEntity<List<EvaluationDTO>> getEvaluationsByUser(@PathVariable Long userId){
        return new ResponseEntity<>(evaluationService.evaluationsByUser(userId), HttpStatus.OK);
    }
}

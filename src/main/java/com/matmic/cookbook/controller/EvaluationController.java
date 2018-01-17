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

/**
 * REST controller for managing Evaluation
 */
@RestController
@RequestMapping("/api")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    /**
     * GET /evaluations : get all evaluations
     *
     * @return ResponseEntity with status 200 and with body list of evaluationDTO
     */
    @GetMapping("/evaluations")
    public ResponseEntity<List<EvaluationDTO>> getAllEvaluations(){
        return new ResponseEntity<>(evaluationService.getEvaluations(), HttpStatus.OK);
    }

    /**
     * GET /evaluation/:id : get one evaluationDTO by id
     *
     * @param id the id of evaluationDTO
     * @return ResponseEntity with status 200 OK and with body evaluationDTO
     */
    @GetMapping("/evaluation/{id}")
    public ResponseEntity<EvaluationDTO> getEvaluationById(@PathVariable Long id){
        EvaluationDTO evaluationDTO = evaluationService.findEvaluationById(id);
        return new ResponseEntity<>(evaluationDTO, HttpStatus.OK);
    }

    /**
     * GET /evaluation/user/:id : get evaluations by user
     *
     * @param userId the id of user
     * @return ResponseEntity with status 200 OK and with body list of evaluationDTO
     */
    @GetMapping("/evaluation/user/{userId}")
    public ResponseEntity<List<EvaluationDTO>> getEvaluationsByUser(@PathVariable Long userId){
        return new ResponseEntity<>(evaluationService.evaluationsByUser(userId), HttpStatus.OK);
    }
}

package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.util.HttpHeadersUtil;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.service.EvaluationService;
import com.matmic.cookbook.service.RatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Evaluation
 */
@RestController
@RequestMapping("/api")
public class EvaluationController {

    private final Logger log = LoggerFactory.getLogger(EvaluationController.class);

    private static final String ENTITY_NAME = "evaluation";

    private final EvaluationService evaluationService;
    private final RatingService ratingService;

    public EvaluationController(EvaluationService evaluationService, RatingService ratingService) {
        this.evaluationService = evaluationService;
        this.ratingService = ratingService;
    }

    /**
     * GET /evaluations : get all evaluations
     *
     * @return ResponseEntity with status 200 and with body list of evaluationDTO
     */
    @GetMapping("/evaluations")
    public ResponseEntity<List<EvaluationDTO>> getAllEvaluations(){
        log.debug("REST request to get all evaluations");
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
        log.debug("REST request to get single evaluation by id: {}", id);
        EvaluationDTO evaluationDTO = evaluationService.findEvaluationById(id);
        return new ResponseEntity<>(evaluationDTO, HttpStatus.OK);
    }

    /**
     * POST /evaluation : add evaluation to recipe rating
     * @param evaluationDTO evaluation to be added to recipe
     * @return ResponseEntity with status 200 OK and body with recipe being evaluated
     * @throws URISyntaxException if the Recipe Location URI syntax is incorrect
     */
    @PostMapping("/evaluation")
    public ResponseEntity<EvaluationDTO> saveNewEvaluation(@RequestBody EvaluationDTO evaluationDTO) throws URISyntaxException {
        if (evaluationDTO.getId() != null){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "New evaluation can not be created, already has an id"))
                    .body(null);
        }
        log.debug("REST request to add evaluation to recipe: {}", evaluationDTO);
        EvaluationDTO savedEvaluation = evaluationService.saveNewEvaluation(evaluationDTO);
        ratingService.updateRating(savedEvaluation);
        return ResponseEntity.created(new URI("/api/evaluation/" + savedEvaluation.getId().toString()))
                .headers(HttpHeadersUtil.createdEntityAlert(ENTITY_NAME, savedEvaluation.getId().toString()))
                .body(savedEvaluation);
    }

    /**
     * PUT /evaluation : update Evaluation
     *
     * @param evaluationDTO evaluation entity to update
     * @return ResponseEntity with status 200 OK and updated evaluation in body,
     * or with status 400 Bad Request if the evaluationDTO is not valid.
     * @throws URISyntaxException if Evaluation Location URI syntax is incorrect
     */
    @PutMapping("/evaluation")
    public ResponseEntity<EvaluationDTO> updateEvaluation(@RequestBody EvaluationDTO evaluationDTO) throws URISyntaxException{
        log.debug("REST request to update evaluation: {}", evaluationDTO);
        if (evaluationDTO.getId() == null){
            return saveNewEvaluation(evaluationDTO);
        }

        EvaluationDTO evaluationUpdated = evaluationService.updateEvaluation(evaluationDTO);

        if (evaluationUpdated == null){
            return ResponseEntity.badRequest()
                    .headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "Evaluation is not valid."))
                    .body(null);
        }

        ratingService.updateRating(evaluationUpdated);
        return ResponseEntity.ok().headers(HttpHeadersUtil.updateEntityAlert(ENTITY_NAME, evaluationUpdated.getId().toString()))
                .body(evaluationUpdated);
    }

    /**
     * GET /evaluation/user/:id : get evaluations by user
     *
     * @param userId the id of user
     * @return ResponseEntity with status 200 OK and with body list of evaluationDTO
     */
    @GetMapping("/evaluation/user/{userId}")
    public ResponseEntity<List<EvaluationDTO>> getEvaluationsByUser(@PathVariable Long userId){
        log.debug("REST request to get user evaluations: {}", userId);
        return new ResponseEntity<>(evaluationService.evaluationsByUser(userId), HttpStatus.OK);
    }
}

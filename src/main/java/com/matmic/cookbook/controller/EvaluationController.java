package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.util.HttpHeadersUtil;
import com.matmic.cookbook.controller.util.PaginationUtil;
import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.repository.EvaluationRepository;
import com.matmic.cookbook.service.EvaluationService;
import com.matmic.cookbook.service.RatingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Evaluation
 */
@RestController
@RequestMapping("/api")
public class EvaluationController {


    private static final String ENTITY_NAME = "evaluation";

    private final EvaluationService evaluationService;
    private final EvaluationRepository evaluationRepository;
    private final RatingService ratingService;


    public EvaluationController(EvaluationService evaluationService, EvaluationRepository evaluationRepository, RatingService ratingService) {
        this.evaluationService = evaluationService;
        this.evaluationRepository = evaluationRepository;
        this.ratingService = ratingService;
    }

    /**
     * GET /evaluations : get all evaluations
     *
     * @param pageable pagination information
     * @return ResponseEntity with status 200 OK and with body list of evaluationDTO
     */
    @GetMapping("/evaluations")
    public ResponseEntity<List<EvaluationDTO>> getAllEvaluations(Pageable pageable){
        Page<EvaluationDTO> page = evaluationService.getEvaluations(pageable);
        HttpHeaders headers = PaginationUtil.paginationHttpHeader(page, "/api/evaluations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
        Optional<Evaluation> isExisting = evaluationRepository.findByIdAndUserId(evaluationDTO.getRatingId(), evaluationDTO.getUserId());
        if (isExisting.isPresent()){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "Evaluation exist you can only update it."))
                    .body(null);
        }
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
        return new ResponseEntity<>(evaluationService.evaluationsByUser(userId), HttpStatus.OK);
    }
}

package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.util.HttpHeadersUtil;
import com.matmic.cookbook.dto.UnitOfMeasureDTO;
import com.matmic.cookbook.service.UnitOfMeasureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing UnitOfMeasure
 */
@RestController
@RequestMapping("/api")
public class UnitOfMeasureController {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureController.class);

    private static final String ENTITY_NAME = "UnitOfMeasure";

    private final UnitOfMeasureService uomService;

    public UnitOfMeasureController(UnitOfMeasureService uomService) {
        this.uomService = uomService;
    }

    /**
     * GET /units : get all UnitOfMeasure entities
     *
     * @return list of all entities
     */
    @GetMapping("/units")
    public ResponseEntity<List<UnitOfMeasureDTO>> getAllUnits(){
        log.debug("REST request to get all Units");
        return new ResponseEntity<>(uomService.getUoMList(), HttpStatus.OK);
    }

    /**
     * POST /unit/new : create new UnitOfMeasure
     *
     * @param newUom entity to be saved
     * @return the ResponseEntity with status 201 Created and with body of saved unitOfMeasure,
     * or with status 400 BadRequest if unitOfMeasure has already an ID parameter
     * @throws URISyntaxException if new UnitOfMeasure URI syntax is incorrect
     */
    @PostMapping("/unit/new")
    @Secured("ADMIN")
    public ResponseEntity<UnitOfMeasureDTO> createNewUnitOfMeasure(@RequestBody UnitOfMeasureDTO newUom)
            throws URISyntaxException{
        log.debug("REST request to create and save new UnitOfMeasure entity");
        if (newUom.getId() != null){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil
                    .createEntityFailureAlert(ENTITY_NAME, "New unit cannot be created, already have an ID"))
                    .body(null);
        }
        UnitOfMeasureDTO uomToSave = uomService.save(newUom);
        return ResponseEntity.created(new URI("/api/unit/" + uomToSave.getId()))
                .headers(HttpHeadersUtil.createdEntityAlert(ENTITY_NAME, uomToSave.getId().toString()))
                .body(uomToSave);
    }

    /**
     * GET /unit/:id : get unitOfMeasure by id
     *
     * @param id id of the unit
     * @return the ResponseEntity with status 200 OK and unitOfMeasure in body
     */
    @GetMapping("/unit/by-id/{id}")
    public ResponseEntity<UnitOfMeasureDTO> getUnitById(@PathVariable Long id){
        log.debug("REST request to get UnitOfMeasure by id: {}", id);
        return new ResponseEntity<>(uomService.findUnitById(id), HttpStatus.OK);
    }


    /**
     * GET /unit/:name : get unitOfMeasure by name
     *
     * @param name name of unit
     * @return the ResponseEntity with status 200 OK and unitOfMeasure in body
     */
    @GetMapping("/unit/by-name/{name}")
    public ResponseEntity<UnitOfMeasureDTO> getUnitByName(@PathVariable String name){
        log.debug("REST request to get UnitOfMeasure by name: {}", name);
        return new ResponseEntity<>(uomService.findUnitByName(name), HttpStatus.OK);
    }

    /**
     * DELETE /unit/:id : delete UnitOfMeasure by id
     *
     * @param id unit to delete id
     * @return the ResponseEntity with status 200 OK
     */
    @DeleteMapping("/unit/{id}")
    @Secured("ADMIN")
    public ResponseEntity<Void> deleteUnitOfMeasureById(@PathVariable Long id){
        log.debug("REST request to delete UnitOfMeasure by id: {}", id);
        uomService.deleteUnit(id);
        return ResponseEntity.ok().headers(HttpHeadersUtil.deleteEntityAlert(ENTITY_NAME, id.toString()))
                .build();
    }

}

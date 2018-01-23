package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.UnitOfMeasureDtoToUnitOfMeasure;
import com.matmic.cookbook.converter.UnitOfMeasureToUnitOfMeasureDto;
import com.matmic.cookbook.domain.UnitOfMeasure;
import com.matmic.cookbook.dto.UnitOfMeasureDTO;
import com.matmic.cookbook.repository.UnitOfMeasureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing UnitOfMeasure
 */
@Service
@Transactional
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureServiceImpl.class);

    private final UnitOfMeasureRepository uomRepository;
    private final UnitOfMeasureDtoToUnitOfMeasure toUnitOfMeasure;
    private final UnitOfMeasureToUnitOfMeasureDto toUnitOfMeasureDto;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository uomRepository,
                                    UnitOfMeasureDtoToUnitOfMeasure toUnitOfMeasure,
                                    UnitOfMeasureToUnitOfMeasureDto toUnitOfMeasureDto) {
        this.uomRepository = uomRepository;
        this.toUnitOfMeasure = toUnitOfMeasure;
        this.toUnitOfMeasureDto = toUnitOfMeasureDto;
    }

    /**
     * Get all unit of measure
     *
     * @param pageable pagination information
     * @return list of all entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UnitOfMeasureDTO> findAllUoms(Pageable pageable) {
        log.debug("Request to get all Units of Measure");
        return uomRepository.findAll(pageable).map(toUnitOfMeasureDto::convert);
    }

    /**
     * Save UnitOfMeasure entity
     *
     * @param unitOfMeasureDTO entity to be saved
     * @return saved entity
     */
    @Override
    public UnitOfMeasureDTO save(UnitOfMeasureDTO unitOfMeasureDTO) {
        log.debug("Request to save UnitOfMeasure: {}", unitOfMeasureDTO);
        UnitOfMeasure unitToSave = toUnitOfMeasure.convert(unitOfMeasureDTO);
        UnitOfMeasure savedUnit = uomRepository.save(unitToSave);
        return toUnitOfMeasureDto.convert(savedUnit);
    }

    /**
     * Get UnitOfMeasure entity by name
     *
     * @param name name of entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UnitOfMeasureDTO findUnitByName(String name) {
        log.debug("Request to get one UnitOfMeasure by name: {}", name);
        return uomRepository.findUnitOfMeasureByName(name)
                .map(toUnitOfMeasureDto::convert)
                .orElseThrow(NullPointerException::new);
    }

    /**
     *Get UnitOfMeasure entity by id
     *
     * @param id the id of entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UnitOfMeasureDTO findUnitById(Long id) {
        log.debug("Request to get one UnitOfMeasure by id: {}", id);
        return uomRepository.findById(id)
                .map(toUnitOfMeasureDto::convert)
                .orElseThrow(NullPointerException::new);
    }

    /**
     * Delete UnitOfMeasure entity by id
     *
     * @param id entity id
     */
    @Override
    public void deleteUnit(Long id) {
        log.debug("Request to delete UnitOfMeasure entity by id: {}", id);
        uomRepository.deleteById(id);
    }
}

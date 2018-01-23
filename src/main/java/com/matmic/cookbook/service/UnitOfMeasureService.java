package com.matmic.cookbook.service;

import com.matmic.cookbook.dto.UnitOfMeasureDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UnitOfMeasureService {
    Page<UnitOfMeasureDTO> findAllUoms(Pageable pageable);
    UnitOfMeasureDTO save(UnitOfMeasureDTO unitOfMeasureDTO);
    UnitOfMeasureDTO findUnitByName(String name);
    UnitOfMeasureDTO findUnitById(Long id);
    void deleteUnit(Long id);
}

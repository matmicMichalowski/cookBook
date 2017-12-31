package com.matmic.cookbook.service;

import com.matmic.cookbook.dto.UnitOfMeasureDTO;

import java.util.List;

public interface UnitOfMeasureService {
    List<UnitOfMeasureDTO> getUoMList();
    UnitOfMeasureDTO save(UnitOfMeasureDTO unitOfMeasureDTO);
    UnitOfMeasureDTO findUnitByName(String name);
    UnitOfMeasureDTO findUnitById(Long id);
    void deleteUnit(Long id);
}

package com.matmic.cookbook.mapper;

import com.matmic.cookbook.domain.UnitOfMeasure;
import com.matmic.cookbook.dto.UnitOfMeasureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UnitOfMeasureMapper {

    UnitOfMeasureMapper INSTANCE = Mappers.getMapper(UnitOfMeasureMapper.class);

    UnitOfMeasureDTO unitOfMeasureToUnitOfMeasureDto(UnitOfMeasure unitOfMeasure);

    UnitOfMeasure unitOfMeasureDtoToUnitOfMeasure(UnitOfMeasureDTO unitOfMeasureDTO);
}

package com.matmic.cookbook.mapper;

import com.matmic.cookbook.domain.UnitOfMeasure;
import com.matmic.cookbook.dto.UnitOfMeasureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UnitOfMeasureMapper {

    UnitOfMeasureMapper INSTANCE = Mappers.getMapper(UnitOfMeasureMapper.class);

    @Mapping(source = "id", target = "id")
    UnitOfMeasureDTO unitOfMeasureToUnitOfMeasureDto(UnitOfMeasure unitOfMeasure);

    @Mapping(source = "id", target = "id")
    UnitOfMeasure unitOfMeasureDtoToUnitOfMeasure(UnitOfMeasureDTO unitOfMeasureDTO);
}

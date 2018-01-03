package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.UnitOfMeasure;
import com.matmic.cookbook.dto.UnitOfMeasureDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureDtoToUnitOfMeasure implements Converter<UnitOfMeasureDTO, UnitOfMeasure>{
    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasure convert(UnitOfMeasureDTO unitOfMeasureDTO) {
        if (unitOfMeasureDTO == null) {
            return null;
        }

        final UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(unitOfMeasureDTO.getId());
        unitOfMeasure.setName(unitOfMeasureDTO.getName());

        return unitOfMeasure;
    }
}

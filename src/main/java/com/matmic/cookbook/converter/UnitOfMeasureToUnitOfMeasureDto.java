package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.UnitOfMeasure;
import com.matmic.cookbook.dto.UnitOfMeasureDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureToUnitOfMeasureDto implements Converter<UnitOfMeasure, UnitOfMeasureDTO>{
    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasureDTO convert(UnitOfMeasure unitOfMeasure) {
        if (unitOfMeasure == null) {
            return null;
        }

        final UnitOfMeasureDTO unitOfMeasureDTO = new UnitOfMeasureDTO();

        unitOfMeasureDTO.setId(unitOfMeasure.getId());
        unitOfMeasureDTO.setName(unitOfMeasure.getName());

        return unitOfMeasureDTO;
    }
}

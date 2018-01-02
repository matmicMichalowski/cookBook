package com.matmic.cookbook.mapper;

import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.dto.EvaluationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EvaluationMapper {

    EvaluationMapper INSTANCE = Mappers.getMapper(EvaluationMapper.class);

    @Mapping(source = "id", target = "id")
    EvaluationDTO evaluationToEvaluationDto(Evaluation evaluation);

    @Mapping(source = "id", target = "id")
    Evaluation evaluationDtoToEvaluation(EvaluationDTO evaluationDTO);
}

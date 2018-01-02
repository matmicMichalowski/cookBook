package com.matmic.cookbook.mapper;

import com.matmic.cookbook.domain.Ingredient;
import com.matmic.cookbook.dto.IngredientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IngredientMapper {

    IngredientMapper INSTANCE = Mappers.getMapper(IngredientMapper.class);

    @Mapping(source = "id", target = "id")
    IngredientDTO ingredientToIngredientDto(Ingredient ingredient);

    @Mapping(source = "id", target = "id")
    Ingredient ingredientDtoToIngredient(IngredientDTO ingredientDTO);
}

package com.matmic.cookbook.mapper;

import com.matmic.cookbook.domain.Ingredient;
import com.matmic.cookbook.dto.IngredientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IngredientMapper {

    IngredientMapper INSTANCE = Mappers.getMapper(IngredientMapper.class);


    IngredientDTO ingredientToIngredientDto(Ingredient ingredient);

    Ingredient ingredientDtoToIngredient(IngredientDTO ingredientDTO);
}

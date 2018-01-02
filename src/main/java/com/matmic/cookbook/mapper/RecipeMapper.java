package com.matmic.cookbook.mapper;

import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.dto.RecipeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    @Mapping(source = "id", target = "id")
    RecipeDTO recipeToRecipeDto(Recipe recipe);

    @Mapping(source = "id", target = "id")
    Recipe recipeDtoToRecipe(RecipeDTO recipeDTO);
}

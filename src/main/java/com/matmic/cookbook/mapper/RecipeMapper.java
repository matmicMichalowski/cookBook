package com.matmic.cookbook.mapper;

import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.dto.RecipeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecipeMapper {

    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    RecipeDTO recipeToRecipeDto(Recipe recipe);

    Recipe recipeDtoToRecipe(RecipeDTO recipeDTO);
}

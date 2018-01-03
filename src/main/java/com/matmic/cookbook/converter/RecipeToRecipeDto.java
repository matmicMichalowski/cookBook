package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.dto.RecipeDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeToRecipeDto implements Converter<Recipe, RecipeDTO>{


    private final RatingToRatingDto ratingConverter;
    private final CategoryToCategoryDto categoryConverter;
    private final IngredientToIngredientDto ingredientConverter;
    private final CommentToCommentDto commentConverter;

    public RecipeToRecipeDto(RatingToRatingDto ratingConverter, CategoryToCategoryDto categoryConverter,
                             IngredientToIngredientDto ingredientConverter, CommentToCommentDto commentConverter) {
        this.ratingConverter = ratingConverter;
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.commentConverter = commentConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public RecipeDTO convert(Recipe recipe) {
        if (recipe == null) {
            return null;
        }

        final RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(recipe.getId());
        recipeDTO.setUserId(recipe.getUser().getId());
        recipeDTO.setCookTime(recipe.getCookTime());
        recipeDTO.setServings(recipe.getServings());
        recipeDTO.setDifficulty(recipe.getDifficulty());
        recipeDTO.setDirections(recipe.getDirections());
        recipeDTO.setRating(ratingConverter.convert(recipe.getRating()));
        recipeDTO.setName(recipe.getName());

        if(recipe.getCategories() != null && recipe.getCategories().size() > 0){
            recipe.getCategories().forEach(category -> recipeDTO.getCategories()
                    .add(categoryConverter.convert(category)));
        }

        if(recipe.getIngredients() != null && recipe.getIngredients().size() > 0){
            recipe.getIngredients().forEach(ingredientDTO -> recipeDTO.getIngredients()
                    .add(ingredientConverter.convert(ingredientDTO)));
        }

        if(recipe.getComments() != null && recipe.getComments().size() > 0){
            recipe.getComments().forEach(commentDTO -> recipeDTO.getComments()
                    .add(commentConverter.convert(commentDTO)));
        }

        return recipeDTO;
    }
}

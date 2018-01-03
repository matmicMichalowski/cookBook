package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.RecipeDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeDtoToRecipe implements Converter<RecipeDTO, Recipe>{


    private final RatingDtoToRating ratingDtoToRating;
    private final CategoryDtoToCategory categoryDtoToCategory;
    private final IngredientDtoToIngredient ingredientDtoToIngredient;
    private final CommentDtoToComment commentDtoToComment;


    public RecipeDtoToRecipe(RatingDtoToRating ratingDtoToRating, CategoryDtoToCategory categoryDtoToCategory, IngredientDtoToIngredient ingredientDtoToIngredient, CommentDtoToComment commentDtoToComment) {
        this.ratingDtoToRating = ratingDtoToRating;
        this.categoryDtoToCategory = categoryDtoToCategory;
        this.ingredientDtoToIngredient = ingredientDtoToIngredient;
        this.commentDtoToComment = commentDtoToComment;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(RecipeDTO recipeDTO) {
        if (recipeDTO == null) {
            return null;
        }

        final Recipe recipe = new Recipe();
        recipe.setId(recipeDTO.getId());
        recipe.setCookTime(recipeDTO.getCookTime());
        recipe.setServings(recipeDTO.getServings());
        recipe.setDifficulty(recipeDTO.getDifficulty());
        recipe.setDirections(recipeDTO.getDirections());
        recipe.setRating(ratingDtoToRating.convert(recipeDTO.getRating()));
        recipe.setName(recipeDTO.getName());

        if(recipeDTO.getCategories() != null && recipeDTO.getCategories().size() > 0){
            recipeDTO.getCategories().forEach(categoryDTO -> recipe.getCategories()
                    .add(categoryDtoToCategory.convert(categoryDTO)));
        }

        if(recipeDTO.getIngredients() != null && recipeDTO.getIngredients().size() > 0){
            recipeDTO.getIngredients().forEach(ingredientDTO -> recipe.getIngredients()
                    .add(ingredientDtoToIngredient.convert(ingredientDTO)));
        }

        if(recipeDTO.getComments() != null && recipeDTO.getComments().size() > 0){
            recipeDTO.getComments().forEach(commentDTO -> {
                recipe.getComments().add(commentDtoToComment.convert(commentDTO));
            });
        }

        if (recipeDTO.getUserId() != null){
            User user = new User();
            user.setId(recipeDTO.getUserId());
            recipe.setUser(user);
            user.getRecipes().add(recipe);
        }

        return recipe;
    }
}

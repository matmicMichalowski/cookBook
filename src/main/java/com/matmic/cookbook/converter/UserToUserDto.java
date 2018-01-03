package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.UserDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDto implements Converter<User, UserDTO> {

    private final CommentToCommentDto commentConverter;
    private final EvaluationToEvaluationDto evaluationConverter;
    private final RecipeToRecipeDto recipeConverter;

    public UserToUserDto(CommentToCommentDto commentConverter, EvaluationToEvaluationDto evaluationConverter, RecipeToRecipeDto recipeConverter) {
        this.commentConverter = commentConverter;
        this.evaluationConverter = evaluationConverter;
        this.recipeConverter = recipeConverter;
    }


    @Synchronized
    @Nullable
    @Override
    public UserDTO convert(User user) {
        if (user == null) {
            return null;
        }

        final UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setPassword(user.getPassword());

        if (user.getComments() != null && user.getComments().size() > 0) {
            user.getComments().forEach(comment -> userDTO.getComments()
                    .add(commentConverter.convert(comment)));
        }

        if (user.getEvaluations() != null && user.getEvaluations().size() > 0) {
            user.getEvaluations().forEach(evaluation -> userDTO.getEvaluations()
                    .add(evaluationConverter.convert(evaluation)));
        }

        if (user.getRecipes() != null && user.getEvaluations().size() > 0) {
            user.getRecipes().forEach(recipe -> userDTO.getRecipes()
                    .add(recipeConverter.convert(recipe)));
        }

        return userDTO;
    }
}

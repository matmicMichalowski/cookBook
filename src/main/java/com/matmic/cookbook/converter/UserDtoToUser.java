package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.UserDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUser implements Converter<UserDTO, User>{

    private final CommentDtoToComment commentConverter;
    private final EvaluationDtoToEvaluation evaluationConverter;
    private final RecipeDtoToRecipe recipeConverter;

    public UserDtoToUser(CommentDtoToComment commentConverter, EvaluationDtoToEvaluation evaluationConverter, RecipeDtoToRecipe recipeConverter) {
        this.commentConverter = commentConverter;
        this.evaluationConverter = evaluationConverter;
        this.recipeConverter = recipeConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public User convert(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        final User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());

        if(userDTO.getComments() != null && userDTO.getComments().size() > 0){
            userDTO.getComments().forEach(commentDTO -> user.getComments()
                    .add(commentConverter.convert(commentDTO)));
        }

        if (userDTO.getEvaluations() != null && userDTO.getEvaluations().size() > 0){
            userDTO.getEvaluations().forEach(evaluationDTO -> user.getEvaluations()
            .add(evaluationConverter.convert(evaluationDTO)));
        }

        if (userDTO.getRecipes() != null && userDTO.getEvaluations().size() > 0){
            userDTO.getRecipes().forEach(recipeDTO -> user.getRecipes()
            .add(recipeConverter.convert(recipeDTO)));
        }

        return user;
    }
}

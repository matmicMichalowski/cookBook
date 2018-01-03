package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Comment;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.CommentDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CommentDtoToComment implements Converter<CommentDTO, Comment>{


    @Synchronized
    @Nullable
    @Override
    public Comment convert(CommentDTO commentDTO) {
        if (commentDTO == null) {
            return null;
        }

        final Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setComment(commentDTO.getComment());
        if(commentDTO.getRecipeId() != null){
            Recipe recipe = new Recipe();
            recipe.setId(commentDTO.getRecipeId());
            comment.setRecipe(recipe);
            recipe.getComments().add(comment);
        }

        if (commentDTO.getUserId() != null){
            User user = new User();
            user.setId(commentDTO.getUserId());
            comment.setUser(user);
            user.getComments().add(comment);
        }


        return comment;
    }
}
